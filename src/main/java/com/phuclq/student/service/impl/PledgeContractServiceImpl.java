package com.phuclq.student.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuclq.student.domain.*;
import com.phuclq.student.dto.CustomerDto;
import com.phuclq.student.dto.FeesDto;
import com.phuclq.student.dto.PledgeContractDto;
import com.phuclq.student.mapper.PledgeContractMapper;
import com.phuclq.student.repository.*;
import com.phuclq.student.service.FileStorageService;
import com.phuclq.student.service.FileUploadResult;
import com.phuclq.student.service.PledgeContractService;
import com.phuclq.student.service.S3StorageService;
import com.phuclq.student.types.InterestPaymentType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.phuclq.student.types.FileType.FILE_AVATAR;
import static com.phuclq.student.types.FileType.PLEDGE_CONTRACT_FILE;

@Service
@AllArgsConstructor
public class PledgeContractServiceImpl implements PledgeContractService {

    // (Inject tất cả các Repository cần thiết)
    private final PledgeContractRepository contractRepository;
    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final CollateralAssetRepository collateralRepository;
    private final FeeDetailRepository feeDetailRepository;
    private final AttachmentRepository attachmentRepository;

    private final FileStorageService fileStorageService;
    private final PledgeContractMapper mapper;
    private final ObjectMapper objectMapper;
    private final S3StorageService s3StorageService;
    private final PaymentScheduleRepository paymentScheduleRepository;


    @Override
    @Transactional
    public PledgeContract createPledge(String payloadJson, MultipartFile portraitFile, List<MultipartFile> attachmentFiles) {
        try {
            // 1️⃣ Parse JSON → DTO
            PledgeContractDto dto = objectMapper.readValue(payloadJson, PledgeContractDto.class);

            // 2️⃣ Upload ảnh chân dung (nếu có)
            Attachment portraitUpload = null;
            String portraitUrl = null;
            if (portraitFile != null && !portraitFile.isEmpty()) {
                portraitUpload = s3StorageService.uploadFileToS3(portraitFile, null, FILE_AVATAR.getName());
                portraitUrl = portraitUpload.getUrl();
            }

            // 3️⃣ Lưu Customer (tìm hoặc tạo mới)
            Customer savedCustomer = findOrCreateCustomer(dto.getCustomer(), portraitUrl);

            // 4️⃣ Lưu Loan
            Loan loanEntity = mapper.toLoanEntity(dto.getLoan());
            if (!InterestPaymentType.isValid(loanEntity.getInterestPaymentType())) {
                throw new IllegalArgumentException("Loại thanh toán lãi không hợp lệ: " + loanEntity.getInterestPaymentType());
            }
            Loan savedLoan = loanRepository.save(loanEntity);

            // 5️⃣ Lưu CollateralAsset (Tài sản thế chấp)
            CollateralAsset collateralEntity = mapper.toCollateralAssetEntity(dto.getCollateral());
            CollateralAsset savedCollateral = collateralRepository.save(collateralEntity);

            // 6️⃣ Tạo và lưu Hợp đồng chính
            PledgeContract contractEntity = PledgeContract.builder()
                    .storeId(dto.getStoreId())
                    .customerId(savedCustomer.getId())
                    .loanId(savedLoan.getId())
                    .collateralId(savedCollateral.getId())
                    .build();

            PledgeContract savedContract = contractRepository.save(contractEntity);

            // 7️⃣ Cập nhật lại liên kết 2 chiều
            savedCollateral.setContractId(savedContract.getId());
            collateralRepository.save(savedCollateral);

            // 8️⃣ Sinh lịch trả lãi (PaymentSchedule)
            generatePaymentSchedule(savedLoan, savedContract.getId());

            // 9️⃣ Lưu các loại phí
            saveFeeDetails(dto.getFees(), savedContract.getId());

            // 🔟 Lưu file đính kèm (nếu có)
            if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
                for (MultipartFile file : attachmentFiles) {
                    if (file == null || file.isEmpty()) continue;
                    try {
                        Attachment uploaded = s3StorageService.uploadFileToS3(file, null, PLEDGE_CONTRACT_FILE.getName());
                        uploaded.setRequestId(savedContract.getId().intValue());
                        attachmentRepository.save(uploaded);
                    } catch (Exception ex) {
                        // Chỉ log lỗi, không rollback toàn bộ
                        System.err.println("⚠️ Upload file thất bại: " + file.getOriginalFilename());
                    }
                }
            }

            // 11️⃣ Lưu ảnh chân dung (nếu có)
            if (portraitUpload != null) {
                portraitUpload.setRequestId(savedContract.getId().intValue());
                attachmentRepository.save(portraitUpload);
            }

            return savedContract;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo hợp đồng: " + e.getMessage(), e);
        }
    }
    private void generatePaymentSchedule(Loan loan, Long contractId) {
        int count = loan.getPaymentCount() != null ? loan.getPaymentCount() : 1;
        BigDecimal principal = loan.getLoanAmount();
        LocalDate startDate = loan.getLoanDate();
        int termValue = loan.getInterestTermValue() != null ? loan.getInterestTermValue() : 30;

        BigDecimal interestPerPeriod = calculateInterestPerPeriod(loan);

        for (int i = 1; i <= count; i++) {
            LocalDate dueDate = startDate.plusDays(termValue * i);

            BigDecimal principalAmount = BigDecimal.ZERO;
            if ("INSTALLMENT".equalsIgnoreCase(loan.getInterestPaymentType())) {
                principalAmount = principal.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
            } else if ("LUMP_SUM_END".equalsIgnoreCase(loan.getInterestPaymentType()) && i == count) {
                principalAmount = principal;
            }

            BigDecimal totalAmount = interestPerPeriod.add(principalAmount);

            PaymentSchedule schedule = PaymentSchedule.builder()
                    .contractId(contractId)
                    .periodNumber(i)
                    .dueDate(dueDate)
                    .interestAmount(interestPerPeriod)
                    .principalAmount(principalAmount)
                    .totalAmount(totalAmount)
                    .status("PENDING")
                    .build();

            paymentScheduleRepository.save(schedule);
        }
    }

    private BigDecimal calculateInterestPerPeriod(Loan loan) {
        BigDecimal ratePerMillionPerDay = loan.getInterestRateValue();
        BigDecimal loanAmount = loan.getLoanAmount();
        BigDecimal million = BigDecimal.valueOf(1_000_000);
        BigDecimal principalInMillions = loanAmount.divide(million, RoundingMode.HALF_UP);

        int days = loan.getInterestTermValue() != null ? loan.getInterestTermValue() : 30;
        return ratePerMillionPerDay.multiply(principalInMillions).multiply(BigDecimal.valueOf(days));
    }


    /**
     * Hàm helper: Tìm khách hàng bằng SĐT/CCCD, nếu không có thì tạo mới
     */
    private Customer findOrCreateCustomer(CustomerDto dto, String portraitUrl) {
        if (dto == null) {
            throw new IllegalArgumentException("Thông tin khách hàng không được rỗng");
        }

        Optional<Customer> existing = Optional.empty();

        if (dto.getIdentityNumber() != null && !dto.getIdentityNumber().isEmpty()) {
            existing = customerRepository.findByIdentityNumber(dto.getIdentityNumber());
        }

        if (existing.isEmpty() && dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
            existing = customerRepository.findByPhoneNumber(dto.getPhoneNumber());
        }

        Customer customerToSave;
        if (existing.isPresent()) {
            customerToSave = existing.get();
            // (Bạn có thể thêm logic cập nhật thông tin khách hàng cũ ở đây nếu muốn)
        } else {
            customerToSave = mapper.toCustomerEntity(dto);
        }

        // Luôn cập nhật/gán ảnh chân dung mới nhất (nếu có upload)
        if (portraitUrl != null) {
            customerToSave.setIdUrl(portraitUrl);
        }

        return customerRepository.save(customerToSave);
    }

    /**
     * Hàm helper: Lưu 4 loại phí vào bảng FeeDetail
     */
    private void saveFeeDetails(FeesDto feesDto, Long contractId) {
        if (feesDto == null) return;

        // 1. Phí kho
        if (feesDto.getWarehouseFee() != null) {
            FeeDetail fee = new FeeDetail();
            fee.setContractId(contractId);
            fee.setFeeType("warehouseFee");
            fee.setValueType(feesDto.getWarehouseFee().getType());
            fee.setValue(feesDto.getWarehouseFee().getValue());
            feeDetailRepository.save(fee);
        }

        // 2. Phí lưu kho
        if (feesDto.getStorageFee() != null) {
            FeeDetail fee = new FeeDetail();
            fee.setContractId(contractId);
            fee.setFeeType("storageFee");
            fee.setValueType(feesDto.getStorageFee().getType());
            fee.setValue(feesDto.getStorageFee().getValue());
            feeDetailRepository.save(fee);
        }

        // 3. Phí rủi ro
        if (feesDto.getRiskFee() != null) {
            FeeDetail fee = new FeeDetail();
            fee.setContractId(contractId);
            fee.setFeeType("riskFee");
            fee.setValueType(feesDto.getRiskFee().getType());
            fee.setValue(feesDto.getRiskFee().getValue());
            feeDetailRepository.save(fee);
        }

        // 4. Phí quản lý
        if (feesDto.getManagementFee() != null) {
            FeeDetail fee = new FeeDetail();
            fee.setContractId(contractId);
            fee.setFeeType("managementFee");
            fee.setValueType(feesDto.getManagementFee().getType());
            fee.setValue(feesDto.getManagementFee().getValue());
            feeDetailRepository.save(fee);
        }
    }
}