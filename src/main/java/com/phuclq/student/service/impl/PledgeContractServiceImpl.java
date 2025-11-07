package com.phuclq.student.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuclq.student.domain.*;
import com.phuclq.student.dto.*;
import com.phuclq.student.mapper.PledgeContractMapper;
import com.phuclq.student.repository.*;
import com.phuclq.student.service.FileStorageService;
import com.phuclq.student.service.FileUploadResult;
import com.phuclq.student.service.PledgeContractService;
import com.phuclq.student.service.S3StorageService;
import com.phuclq.student.types.InterestPaymentType;
import com.phuclq.student.types.LoanStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final PledgeRepository pledgeRepository;
    private final CollateralAttributeRepository collateralAttributeRepository;


    @Override
    @Transactional
    public PledgeContract createPledge(String payloadJson, MultipartFile portraitFile, List<MultipartFile> attachmentFiles) {
        try {
            // 1️⃣ Parse JSON → DTO
            PledgeContractDto dto = objectMapper.readValue(payloadJson, PledgeContractDto.class);
            Long storeId = dto.getStoreId();

            // 2️⃣ Upload ảnh chân dung (nếu có)
            Attachment portraitUpload = null;
            String portraitUrl = null;
            if (portraitFile != null && !portraitFile.isEmpty()) {
                // portraitUpload = s3StorageService.uploadFileToS3(portraitFile, null, FILE_AVATAR.getName());
                portraitUrl = "portraitUpload.getUrl()";
            }

            // 3️⃣ Lưu Customer (tìm hoặc tạo mới)
            Customer savedCustomer = findOrCreateCustomer( storeId,dto.getCustomer(), portraitUrl);

            // 4️⃣ Lưu Loan
            Loan loanEntity = mapper.toLoanEntity(storeId,dto.getLoan());

            Loan savedLoan = loanRepository.save(loanEntity);

            // 5️⃣ Lưu danh sách tài sản thế chấp
            List<CollateralAsset> savedCollaterals = new ArrayList<>();

            if (dto.getCollateral() != null && !dto.getCollateral().isEmpty()) {
                for (CollateralDto colDto : dto.getCollateral()) {
                    // Lưu asset trước
                    CollateralAsset entity = mapper.toCollateralAssetEntity(storeId,colDto);
                    CollateralAsset saved = collateralRepository.save(entity);
                    savedCollaterals.add(saved);

                    // Sau khi lưu asset thì lưu các attributes đi kèm
                    if (colDto.getAttributes() != null && !colDto.getAttributes().isEmpty()) {
                        List<CollateralAttribute> attributes = colDto.getAttributes().stream()
                                .map(attr -> mapper.toCollateralAttributeEntity(attr, saved.getId()))
                                .collect(Collectors.toList());
                        collateralAttributeRepository.saveAll(attributes);
                    }
                }
            }


            // 6️⃣ Tạo và lưu hợp đồng chính
            PledgeContract contractEntity = PledgeContract.builder()
                    .storeId(dto.getStoreId())
                    .customerId(savedCustomer.getId())
                    .loanId(savedLoan.getId())
                    .build();

            contractEntity.setContractCode(generateContractCode());

            PledgeContract savedContract = contractRepository.save(contractEntity);

            // 7️⃣ Cập nhật lại liên kết 2 chiều giữa contract ↔ collaterals
            for (CollateralAsset asset : savedCollaterals) {
                asset.setContractId(savedContract.getId());
                collateralRepository.save(asset);
            }

            // 8️⃣ Sinh lịch trả lãi (PaymentSchedule)
            generatePaymentSchedule(savedLoan, savedContract.getId());

            // 9️⃣ Lưu thông tin các loại phí
            saveFeeDetails(dto.getFees(), savedContract.getId());

            // 🔟 Lưu file đính kèm (nếu có)
//        if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
//            for (MultipartFile file : attachmentFiles) {
//                if (file == null || file.isEmpty()) continue;
//                try {
//                    Attachment uploaded = s3StorageService.uploadFileToS3(file, null, PLEDGE_CONTRACT_FILE.getName());
//                    uploaded.setRequestId(savedContract.getId().intValue());
//                    attachmentRepository.save(uploaded);
//                } catch (Exception ex) {
//                    System.err.println("⚠️ Upload file thất bại: " + file.getOriginalFilename());
//                }
//            }
//        }

            // 11️⃣ Lưu ảnh chân dung (nếu có)
//        if (portraitUpload != null) {
//            portraitUpload.setRequestId(savedContract.getId().intValue());
//            attachmentRepository.save(portraitUpload);
//        }

            return savedContract;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo hợp đồng: " + e.getMessage(), e);
        }
    }


    @Override
    public PledgeContractDetailResponse getPledgeDetail(Long id) {
        return null;// pledgeRepository.findDetailById(id);
    }

    @Override
    public Page<PledgeContractListResponse> searchPledges(PledgeSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        LoanStatus loanStatus = null;
        if (request.getLoanStatus() != null && !request.getLoanStatus().isEmpty()) {
            try {
                loanStatus = LoanStatus.valueOf(request.getLoanStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore invalid value
            }
        }

        return pledgeRepository.searchPledges(
                request.getKeyword(),         // 🔹 Từ khóa tìm kiếm
                request.getLoanStatus(),      // 🔹 Trạng thái khoản vay (LoanStatus)
                request.getStoreId() != null ? Long.valueOf(request.getStoreId()) : null, // 🔹 Cửa hàng
                request.getFromDate(),        // 🔹 Ngày bắt đầu
                request.getToDate(),          // 🔹 Ngày kết thúc
                request.getFollower(),        // 🔹 Người phụ trách
                request.getPledgeStatus(),    // 🔹 Trạng thái hợp đồng (Đang vay, Quá hạn, Đóng, v.v.)
                pageable                      // 🔹 Phân trang
        );
    }


    private void generatePaymentSchedule(Loan loan, Long contractId) {
        // 👉 Số kỳ trả (ví dụ: trả góp 3 kỳ, 6 kỳ...)
        int count = loan.getPaymentCount() != null ? loan.getPaymentCount() : 1;

        // 👉 Số tiền vay gốc
        BigDecimal principal = loan.getLoanAmount();

        // 👉 Ngày bắt đầu tính (ngày giải ngân / ngày vay)
        LocalDate startDate = loan.getLoanDate();

        // 👉 Giá trị 1 kỳ (ví dụ 1 ngày, 1 tuần, 1 tháng,...)
        int termValue = loan.getInterestTermValue() != null ? loan.getInterestTermValue() : 1;

        // 👉 Đơn vị kỳ hạn (Ngày / Tuần / Tháng / Tháng định kỳ)
        String termUnit = loan.getInterestTermUnit() != null ? loan.getInterestTermUnit().name(): "DAY";

        // 👉 Tiền lãi phải trả cho mỗi kỳ
        BigDecimal interestPerPeriod = calculateInterestPerPeriod(loan);

        // 👉 Vòng lặp tạo từng kỳ trả (1 → count)
        for (int i = 1; i <= count; i++) {

            // 👉 Xác định ngày đến hạn theo đơn vị kỳ hạn
            LocalDate dueDate = calculateDueDate(startDate, termValue, termUnit, i);

            // 👉 Tiền gốc phải trả trong kỳ này
            BigDecimal principalAmount = BigDecimal.ZERO;

            // 👉 Nếu loại trả là "trả góp từng kỳ"
            if ("INSTALLMENT".equalsIgnoreCase(loan.getInterestPaymentType().name())) {
                principalAmount = principal.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
            }
            // 👉 Nếu loại trả là "trả gốc cuối kỳ"
            else if ("LUMP_SUM_END".equalsIgnoreCase(loan.getInterestPaymentType().name()) && i == count) {
                principalAmount = principal;
            }

            // 👉 Tổng tiền phải trả kỳ này = gốc + lãi
            BigDecimal totalAmount = interestPerPeriod.add(principalAmount);

            // 👉 Tạo đối tượng PaymentSchedule (1 dòng = 1 kỳ trả)
            PaymentSchedule schedule = PaymentSchedule.builder()
                    .contractId(contractId)
                    .periodNumber(i)
                    .dueDate(dueDate)
                    .interestAmount(interestPerPeriod)
                    .principalAmount(principalAmount)
                    .totalAmount(totalAmount)
                    .status("PENDING")
                    .build();

            // 👉 Lưu vào DB
            paymentScheduleRepository.save(schedule);
        }
    }
    /**
     * Tính ngày đến hạn cho từng kỳ, dựa vào đơn vị kỳ hạn.
     */
    private LocalDate calculateDueDate(LocalDate startDate, int termValue, String termUnit, int periodIndex) {
        switch (termUnit.toUpperCase()) {
            case "DAY":
                return startDate.plusDays((long) termValue * periodIndex);

            case "WEEK":
                return startDate.plusWeeks((long) termValue * periodIndex);

            case "MONTH":
                return startDate.plusMonths((long) termValue * periodIndex);

            case "PERIODIC_MONTH":
                // "Tháng định kỳ" — giữ nguyên ngày trong tháng, cộng thêm theo kỳ
                return startDate.plusMonths(periodIndex);

            default:
                // Mặc định cộng theo ngày nếu đơn vị không xác định
                return startDate.plusDays((long) termValue * periodIndex);
        }
    }



    /**
     * Tính số tiền lãi phải trả cho mỗi kỳ.
     *
     * Công thức cơ bản:
     *   Lãi kỳ = (Tiền vay / 1.000.000) * Lãi/triệu/ngày * Số ngày trong kỳ
     *
     * Ghi chú:
     *   - Nếu đơn vị lãi là "Lãi/triệu/ngày" → tính theo ngày.
     *   - Nếu kỳ hạn là tuần hoặc tháng → quy đổi tương ứng sang số ngày.
     */
    private BigDecimal calculateInterestPerPeriod(Loan loan) {

        // 👉 Lãi suất (ví dụ: 2 nghĩa là 2.000đ / triệu / ngày)
        BigDecimal ratePerMillionPerDay = loan.getInterestRateValue();

        // 👉 Tổng tiền vay (VD: 10.000.000)
        BigDecimal loanAmount = loan.getLoanAmount();

        // 👉 Quy đổi tiền vay sang triệu đồng
        BigDecimal million = BigDecimal.valueOf(1_000_000);
        BigDecimal principalInMillions = loanAmount.divide(million, RoundingMode.HALF_UP);

        // 👉 Số ngày trong mỗi kỳ (mặc định 30 ngày nếu null)
        int termValue = loan.getInterestTermValue() != null ? loan.getInterestTermValue() : 30;
        String termUnit = loan.getInterestTermUnit() != null ? loan.getInterestTermUnit().name() : "DAY";

        // 👉 Quy đổi kỳ hạn ra số ngày thực tế để tính lãi
        int totalDays;
        switch (termUnit.toUpperCase()) {
            case "DAY":
                totalDays = termValue;
                break;
            case "WEEK":
                totalDays = termValue * 7;
                break;
            case "MONTH":
            case "PERIODIC_MONTH":
                totalDays = termValue * 30; // Quy ước trung bình 30 ngày/tháng
                break;
            default:
                totalDays = termValue;
        }

        // 👉 Công thức tính lãi cho 1 kỳ
        BigDecimal interestPerPeriod = ratePerMillionPerDay
                .multiply(principalInMillions)
                .multiply(BigDecimal.valueOf(totalDays));

        return interestPerPeriod.setScale(0, RoundingMode.HALF_UP); // Làm tròn đến đồng
    }



    /**
     * Hàm helper: Tìm khách hàng bằng SĐT/CCCD, nếu không có thì tạo mới
     */
    private Customer findOrCreateCustomer(Long storeId,CustomerDto dto, String portraitUrl) {
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

            customerToSave = mapper.toCustomerEntity( storeId,dto);
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
    private String generateContractCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long countToday = contractRepository.countByCreatedDateBetween(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );
        String sequencePart = String.format("%03d", countToday + 1);
        return "PLEDGE-" + datePart + "-" + sequencePart;
    }
}