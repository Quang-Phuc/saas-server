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
import com.phuclq.student.types.InterestRateUnit;
import com.phuclq.student.types.InterestTermUnit;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.phuclq.student.types.FileType.FILE_AVATAR;
import static com.phuclq.student.types.FileType.PLEDGE_CONTRACT_FILE;
import static com.phuclq.student.types.InterestRateUnit.INTEREST_PER_DAY;
import static com.phuclq.student.types.PaymentType.PAYMENT_PROCESSING;

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

            if (dto.getCollateral() != null) {
                for (CollateralDto colDto : dto.getCollateral()) {

                    CollateralAsset entity = mapper.toCollateralAssetEntity(storeId, colDto);

                    // ⭐ NEW: bổ sung lưu warehouseDailyFee
                    entity.setWarehouseDailyFee(colDto.getWarehouseDailyFee());

                    CollateralAsset saved = collateralRepository.save(entity);
                    savedCollaterals.add(saved);

                    // Attributes
                    if (colDto.getAttributes() != null) {
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
            generatePaymentSchedule(savedLoan,  savedCollaterals);

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
    private BigDecimal calculateWarehouseFee(List<CollateralAsset> assets, LocalDate start, LocalDate end) {
        if (assets == null || assets.isEmpty()) return BigDecimal.ZERO;

        long days = daysBetween(start, end);

        BigDecimal dailyTotal = assets.stream()
                .map(a -> a.getWarehouseDailyFee() != null ? a.getWarehouseDailyFee() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return dailyTotal
                .multiply(BigDecimal.valueOf(days))
                .setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateWarehouseFeeForPeriod(List<CollateralAsset> assets,
                                                      LocalDate startDate,
                                                      LocalDate endDate) {
        if (assets == null || assets.isEmpty()) {
            return BigDecimal.ZERO;
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalDailyFee = assets.stream()
                .map(a -> a.getWarehouseDailyFee() != null ? a.getWarehouseDailyFee() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalDailyFee
                .multiply(BigDecimal.valueOf(days))
                .setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePrincipalForPeriod(Loan loan, int index, int total) {
        BigDecimal principal = loan.getLoanAmount();
        InterestPaymentType type = loan.getInterestPaymentType();

        if (type == null) type = InterestPaymentType.PERIODIC_INTEREST;

        switch (type) {
            case INSTALLMENT:
                BigDecimal base = principal.divide(BigDecimal.valueOf(total), 0, RoundingMode.DOWN);
                BigDecimal remainder = principal.subtract(base.multiply(BigDecimal.valueOf(total)));
                return (index == total) ? base.add(remainder) : base;

            case PERIODIC_INTEREST:
            default:
                return (index == total) ? principal : BigDecimal.ZERO;
        }
    }


    private LocalDate addTerm(LocalDate base, int termValue, InterestTermUnit unit, int index) {
        if (unit == null) unit = InterestTermUnit.MONTH;
        if (termValue <= 0) termValue = 1;

        switch (unit) {
            case DAY:
                return base.plusDays((long) termValue * index);
            case WEEK:
                return base.plusWeeks((long) termValue * index);
            case YEAR:
                return base.plusYears((long) termValue * index);
            default:
                return base.plusMonths((long) termValue * index);
        }
    }

    private long daysBetween(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        return Math.max(days, 0);
    }

    private BigDecimal calculateInterestForPeriod(Loan loan, LocalDate startDate, LocalDate endDate) {
        long days = daysBetween(startDate, endDate);
        if (days <= 0) return BigDecimal.ZERO;

        BigDecimal amount = loan.getLoanAmount();
        BigDecimal rateValue = loan.getInterestRateValue();
        InterestRateUnit unit = loan.getInterestRateUnit();

        switch (unit) {

            case INTEREST_PER_MILLION_PER_DAY:
                BigDecimal millions = amount.divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
                return rateValue.multiply(millions)
                        .multiply(BigDecimal.valueOf(days))
                        .setScale(0, RoundingMode.HALF_UP);

            case INTEREST_PERCENT_PER_MONTH:
                BigDecimal rateMonthly = rateValue.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
                BigDecimal ratio = BigDecimal.valueOf(days).divide(BigDecimal.valueOf(30), 10, RoundingMode.HALF_UP);
                return amount.multiply(rateMonthly).multiply(ratio)
                        .setScale(0, RoundingMode.HALF_UP);

            case INTEREST_PER_DAY:
                BigDecimal dailyRate = rateValue.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
                return amount.multiply(dailyRate)
                        .multiply(BigDecimal.valueOf(days))
                        .setScale(0, RoundingMode.HALF_UP);

            default:
                return BigDecimal.ZERO;
        }
    }



    @Override
    public PledgeContractDetailResponse getPledgeDetail(Long id) {
        return null;// pledgeRepository.findDetailById(id);
    }

    @Override
    public Page<PledgeContractListResponse> searchPledges(PledgeSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        LocalDateTime startDate = request.getFromDate();
        LocalDateTime endDate = request.getToDate();

        if (startDate != null) {
            startDate = startDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
        }

        if (endDate != null) {
            endDate = endDate.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        }



        return pledgeRepository.searchPledges(
                request.getKeyword(),         // 🔹 Từ khóa tìm kiếm
                request.getLoanStatus(),      // 🔹 Trạng thái khoản vay (LoanStatus)
                request.getStoreId(), // 🔹 Cửa hàng
                startDate,        // 🔹 Ngày bắt đầu
                endDate,          // 🔹 Ngày kết thúc
                request.getFollower(),        // 🔹 Người phụ trách
                request.getPledgeStatus(),    // 🔹 Trạng thái hợp đồng (Đang vay, Quá hạn, Đóng, v.v.)
                pageable                      // 🔹 Phân trang
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PledgeContractDto getContractDetail(Long id) {
        PledgeContract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng với id: " + id));

        // Lấy Loan, Customer, Collateral, Fees, PaymentSchedule
        Loan loan = loanRepository.findById(contract.getLoanId()).orElse(null);
        Customer customer = customerRepository.findById(contract.getCustomerId()).orElse(null);
        List<CollateralAsset> collaterals = collateralRepository.findByContractId(contract.getId());
        List<FeeDetail> feeDetails = feeDetailRepository.findByContractId(contract.getId());
        List<PaymentSchedule> schedules = paymentScheduleRepository.findByContractId(contract.getId());

        // Map sang DTO
        PledgeContractDto dto = new PledgeContractDto();
        dto.setId(contract.getId());
        dto.setContractCode(contract.getContractCode());
        dto.setStoreId(contract.getStoreId());

        // Map customer
        if (customer != null) {
            dto.setCustomer(mapper.toCustomerDto(customer));
        }

        // Map loan
        if (loan != null) {
            dto.setLoan(mapper.toLoanDto(loan));
        }

        // Map collaterals
        List<CollateralDto> collateralDtos = collaterals.stream().map(asset -> {
            CollateralDto colDto = mapper.toCollateralDto(asset);
            List<CollateralAttribute> attrs = collateralAttributeRepository.findByCollateralAssetId(asset.getId());
            colDto.setAttributes(attrs.stream()
                    .map(mapper::toCollateralAttributeDto)
                    .collect(Collectors.toList()));
            return colDto;
        }).collect(Collectors.toList());
        dto.setCollateral(collateralDtos);

        // Map fees
        FeesDto feesDto = new FeesDto();
        feeDetails.forEach(f -> {
            switch (f.getFeeType()) {
                case "warehouseFee":
                    feesDto.setWarehouseFee(new FeeItemDto(f.getValueType(), f.getValue()));
                    break;
                case "storageFee":
                    feesDto.setStorageFee(new FeeItemDto(f.getValueType(), f.getValue()));
                    break;
                case "riskFee":
                    feesDto.setRiskFee(new FeeItemDto(f.getValueType(), f.getValue()));
                    break;
                case "managementFee":
                    feesDto.setManagementFee(new FeeItemDto(f.getValueType(), f.getValue()));
                    break;
                case "appraisalFee":
                    feesDto.setAppraisalFee(new FeeItemDto(f.getValueType(), f.getValue()));
                    break;
            }
        });
        dto.setFees(feesDto);

        // Map payment schedules
        dto.setPaymentSchedule(schedules.stream()
                .map(mapper::toPaymentScheduleDto)
                .collect(Collectors.toList()));

        return dto;
    }


    private int calculateDaysOfPeriod( int termValue, String termUnit) {
        switch (termUnit.toUpperCase()) {
            case "DAY":
                return termValue;
            case "WEEK":
                return termValue * 7;
            case "MONTH":
            case "PERIODIC_MONTH":
                return termValue * 30;
            default:
                return termValue;
        }
    }

    public List<PaymentSchedule> generatePaymentSchedule(Loan loan, List<CollateralAsset> assets) {

        int count = loan.getPaymentCount() == null ? 1 : loan.getPaymentCount();
        LocalDate loanDate = loan.getLoanDate();

        List<PaymentSchedule> result = new ArrayList<>();

        for (int i = 1; i <= count; i++) {

            LocalDate dueDate = addTerm(
                    loanDate,
                    loan.getInterestTermValue(),
                    loan.getInterestTermUnit(),
                    i
            );

            LocalDate periodStart = (i == 1) ? loanDate : result.get(i - 2).getDueDate();

            BigDecimal interest = calculateInterestForPeriod(loan, periodStart, dueDate);
            BigDecimal principal = calculatePrincipalForPeriod(loan, i, count);
            BigDecimal warehouseFee = calculateWarehouseFee(assets, periodStart, dueDate);

            BigDecimal total = principal.add(interest).add(warehouseFee);

            PaymentSchedule ps = new PaymentSchedule();
            ps.setPeriodNumber(i);
            ps.setDueDate(dueDate);
            ps.setPrincipalAmount(principal);
            ps.setInterestAmount(interest);
            ps.setWarehouseDailyFee(warehouseFee);
            ps.setTotalAmount(total);
            ps.setStatus("PENDING");

            result.add(ps);
        }

        return result;
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
    @Override
    @Transactional
    public PledgeContract updatePledge(Long id, String payloadJson, MultipartFile portraitFile, List<MultipartFile> attachmentFiles) {
        try {
            // 1️⃣ Parse payload
            PledgeContractDto dto = objectMapper.readValue(payloadJson, PledgeContractDto.class);

            // 2️⃣ Tìm hợp đồng cũ
            PledgeContract existingContract = contractRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng ID: " + id));

            // 3️⃣ Upload ảnh chân dung (nếu có)
            String portraitUrl = null;//existingContract.getCustomer() != null ? existingContract.getCustomer().getIdUrl() : null;
            if (portraitFile != null && !portraitFile.isEmpty()) {
                // Ví dụ: portraitUrl = s3StorageService.uploadFileToS3(portraitFile, null, FILE_AVATAR.getName()).getUrl();
                portraitUrl = "portraitUpload.getUrl()";
            }

            // 4️⃣ Cập nhật thông tin Customer
            Customer updatedCustomer = findOrCreateCustomer(dto.getStoreId(), dto.getCustomer(), portraitUrl);
            existingContract.setCustomerId(updatedCustomer.getId());

            // 5️⃣ Cập nhật khoản vay
            Loan updatedLoan = mapper.toLoanEntity(dto.getStoreId(), dto.getLoan());
            updatedLoan.setId(existingContract.getLoanId());
            loanRepository.save(updatedLoan);

            // 6️⃣ Xóa và cập nhật lại danh sách tài sản thế chấp cũ
            collateralRepository.deleteByContractId(id);

            List<CollateralAsset> newCollaterals = new ArrayList<>();
            if (dto.getCollateral() != null) {
                for (CollateralDto colDto : dto.getCollateral()) {
                    CollateralAsset asset = mapper.toCollateralAssetEntity(dto.getStoreId(), colDto);
                    asset.setContractId(existingContract.getId());
                    CollateralAsset saved = collateralRepository.save(asset);
                    newCollaterals.add(saved);

                    if (colDto.getAttributes() != null) {
                        List<CollateralAttribute> attrs = colDto.getAttributes().stream()
                                .map(attr -> mapper.toCollateralAttributeEntity(attr, saved.getId()))
                                .collect(Collectors.toList());
                        collateralAttributeRepository.saveAll(attrs);
                    }
                }
            }

            // 7️⃣ Cập nhật danh sách phí
            feeDetailRepository.deleteByContractId(id);
            saveFeeDetails(dto.getFees(), id);

            // 8️⃣ Cập nhật lịch trả lãi (nếu cần tái sinh)
            paymentScheduleRepository.deleteByContractId(id);
           // generatePaymentSchedule(updatedLoan, id);

            // 9️⃣ Cập nhật các file đính kèm (nếu có)
            // if (attachmentFiles != null) { ... }

            // 🔟 Cập nhật lại entity
            contractRepository.save(existingContract);

            return existingContract;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật hợp đồng: " + e.getMessage(), e);
        }
    }

}