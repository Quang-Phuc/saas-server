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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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


    @Override
    @Transactional // (Rất quan trọng! Nếu lỗi thì rollback tất cả)
    public PledgeContract createPledge(String payloadJson, MultipartFile portraitFile, List<MultipartFile> attachmentFiles) {
        try {
            // 1. Chuyển đổi JSON -> DTO
            PledgeContractDto dto = objectMapper.readValue(payloadJson, PledgeContractDto.class);
//            2. Thông tin cho vay loan
            // 2. Upload ảnh chân dung (nếu có)
            Attachment portraitUpload = s3StorageService.uploadFileToS3(portraitFile, null, FILE_AVATAR.getName());
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
            PledgeContract contractEntity = PledgeContract.builder().storeId(dto.getStoreId()).customerId(savedCustomer.getId()).loanId(savedLoan.getId()).collateralId(savedCollateral.getId()).build();
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

                    Attachment uploadFileToS3 = s3StorageService.uploadFileToS3(portraitFile, null, PLEDGE_CONTRACT_FILE.getName());
                    uploadFileToS3.setRequestId(contractEntity.getId().intValue());
                    attachmentRepository.save(uploadFileToS3);
                }
            }

            // (Nếu ảnh chân dung cũng lưu vào Attachment)
            if (portraitUpload != null) {
                portraitUpload.setRequestId(contractEntity.getId().intValue());
                attachmentRepository.save(portraitUpload);
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