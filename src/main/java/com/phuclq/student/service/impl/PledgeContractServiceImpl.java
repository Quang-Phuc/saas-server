package com.phuclq.student.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phuclq.student.domain.*; // (Entities)
import com.phuclq.student.dto.*; // (DTOs)
import com.phuclq.student.mapper.PledgeContractMapper;
import com.phuclq.student.repository.*; // (Repositories)
import com.phuclq.student.service.FileStorageService;
import com.phuclq.student.service.FileUploadResult;
import com.phuclq.student.service.PledgeContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
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

    @Autowired
    public PledgeContractServiceImpl(
            PledgeContractRepository contractRepository,
            CustomerRepository customerRepository,
            LoanRepository loanRepository,
            CollateralAssetRepository collateralRepository,
            FeeDetailRepository feeDetailRepository,
            AttachmentRepository attachmentRepository,
            FileStorageService fileStorageService,
            PledgeContractMapper mapper)
    {
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
        this.loanRepository = loanRepository;
        this.collateralRepository = collateralRepository;
        this.feeDetailRepository = feeDetailRepository;
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
        this.mapper = mapper;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Hỗ trợ Java 8 Date/Time
    }

    @Override
    @Transactional // (Rất quan trọng! Nếu lỗi thì rollback tất cả)
    public PledgeContract createPledge(String payloadJson,
                                       MultipartFile portraitFile,
                                       List<MultipartFile> attachmentFiles) {
        try {
            // 1. Chuyển đổi JSON -> DTO
            PledgeContractDto dto = objectMapper.readValue(payloadJson, PledgeContractDto.class);

            // 2. Upload ảnh chân dung (nếu có)
            FileUploadResult portraitUpload = fileStorageService.saveFile(portraitFile);
            String portraitUrl = (portraitUpload != null) ? portraitUpload.getUrl() : null;

            // 3. Lưu Customer
            Customer savedCustomer = findOrCreateCustomer(dto.getCustomer(), portraitUrl);

            // 4. Lưu Loan
            Loan loanEntity = mapper.toLoanEntity(dto.getLoan());
            Loan savedLoan = loanRepository.save(loanEntity);

            // 5. Lưu CollateralAsset (Tài sản thế chấp)
            CollateralAsset collateralEntity = mapper.toCollateralAssetEntity(dto.getCollateral());
            // (Lưu ý: contractId sẽ được cập nhật ở bước 7)
            CollateralAsset savedCollateral = collateralRepository.save(collateralEntity);

            // 6. Tạo và Lưu Hợp đồng (PledgeContract)
            PledgeContract contractEntity = PledgeContract.builder()
                    .storeId(dto.getStoreId())
                    .customerId(savedCustomer.getId())
                    .loanId(savedLoan.getId())
                    .collateralId(savedCollateral.getId())
                    .build();
            PledgeContract savedContract = contractRepository.save(contractEntity);

            // 7. Cập nhật contractId cho CollateralAsset (Hoàn tất liên kết 2 chiều)
            savedCollateral.setContractId(savedContract.getId());
            collateralRepository.save(savedCollateral);

            // 8. Lưu FeeDetail (Lưu các dòng phí)
            saveFeeDetails(dto.getFees(), savedContract.getId());

            // 9. Lưu Attachments (File đính kèm)
            if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
                for (MultipartFile file : attachmentFiles) {
                    if (file == null || file.isEmpty()) continue;

                    FileUploadResult fileUpload = fileStorageService.saveFile(file);

                    Attachment attachment = new Attachment();
                    attachment.setRequestId(savedContract.getId().intValue()); // <-- Sửa kiểu Integer
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setUrl(fileUpload.getUrl());
                    attachment.setFileType(file.getContentType());
                    attachment.setFileNameS3(fileUpload.getS3Key());
                    attachment.setStatus("ACTIVE");
                    attachment.setType("ATTACHMENT"); // (Phân biệt với 'PORTRAIT')

                    attachmentRepository.save(attachment);
                }
            }

            // (Nếu ảnh chân dung cũng lưu vào Attachment)
            if (portraitUpload != null) {
                Attachment portraitAttachment = new Attachment();
                portraitAttachment.setRequestId(savedContract.getId().intValue());
                portraitAttachment.setFileName(portraitFile.getOriginalFilename());
                portraitAttachment.setUrl(portraitUpload.getUrl());
                portraitAttachment.setFileType(portraitFile.getContentType());
                portraitAttachment.setFileNameS3(portraitUpload.getS3Key());
                portraitAttachment.setStatus("ACTIVE");
                portraitAttachment.setType("PORTRAIT"); // <-- Đánh dấu đây là ảnh chân dung
                attachmentRepository.save(portraitAttachment);
            }

            // 10. Trả về Hợp đồng chính
            return savedContract;

        } catch (Exception e) {
            // (Ném RuntimeException để @Transactional có thể rollback)
            throw new RuntimeException("Lỗi khi tạo hợp đồng: " + e.getMessage(), e);
        }
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