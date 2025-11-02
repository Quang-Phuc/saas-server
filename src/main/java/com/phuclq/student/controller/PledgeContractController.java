package com.phuclq.student.controller; // (Thay đổi package cho đúng)

import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.service.PledgeContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pledges")
// @CrossOrigin(origins = "*") // (Thêm nếu cần)
public class PledgeContractController {

    private final PledgeContractService pledgeContractService;

    @Autowired
    public PledgeContractController(PledgeContractService pledgeContractService) {
        this.pledgeContractService = pledgeContractService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPledgeContract(
            @RequestPart("payload") String payloadJson,
            @RequestPart(value = "portrait", required = false) MultipartFile portraitFile,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachmentFiles) {

        try {
            PledgeContract savedContract = pledgeContractService.createPledge(
                    payloadJson,
                    portraitFile,
                    attachmentFiles
            );
            // Trả về hợp đồng đã lưu (hoặc chỉ ID nếu muốn)
            return ResponseEntity.status(HttpStatus.CREATED).body(savedContract);

        } catch (Exception e) {
            // (Nên có @ControllerAdvice để xử lý lỗi chung)
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi xử lý nghiệp vụ: " + e.getMessage()));
        }
    }

    // (Thêm các API GET, PUT, DELETE khác ở đây...)
}