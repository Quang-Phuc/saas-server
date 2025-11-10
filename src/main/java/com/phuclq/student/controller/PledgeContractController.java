package com.phuclq.student.controller; // (Thay đổi package cho đúng)

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.dto.PledgeContractDto;
import com.phuclq.student.dto.PledgeContractListResponse;
import com.phuclq.student.dto.PledgeContractListResponseImpl;
import com.phuclq.student.dto.PledgeSearchRequest;
import com.phuclq.student.repository.PledgeRepository;
import com.phuclq.student.service.PledgeContractService;
import com.phuclq.student.types.LoanStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pledges")
@RequiredArgsConstructor
public class PledgeContractController {

    private final PledgeContractService pledgeContractService;
    private final PledgeRepository pledgeRepository;
    private final RestEntityResponse restEntityRes;


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

    @PostMapping("/search")
    public ResponseEntity<?> searchPledges(@RequestBody PledgeSearchRequest request) {
        Page<PledgeContractListResponse> pledgeContractListResponses = pledgeContractService.searchPledges(request);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(pledgeContractListResponses).getResponse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  getContractDetail(@PathVariable Long id) {
        PledgeContractDto dto = pledgeContractService.getContractDetail(id);
        return  restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(dto).getResponse();
    }

    @GetMapping()
    public  ResponseEntity<?> searchPledgesGet(
            @ModelAttribute PledgeSearchRequest request,
            @RequestParam(name = "mock", defaultValue = "true") boolean mock // bật mock tạm
    ) {
        if (mock) {
            int page = 0 ;
            int size = 10 ;

            PledgeContractListResponse r1 = new PledgeContractListResponseImpl(
                    4L,
                    "CĐ252210-001",
                    LocalDate.now().minusDays(15),
                    LocalDate.now().plusDays(15),
                    "Nguyễn Văn A",
                    "0909000111",
                    "iPhone 15 Pro Max 256GB",
                    new BigDecimal("25000000"),
                    new BigDecimal("5000000"),
                    new BigDecimal("20000000"),
                    LoanStatus.BINH_THUONG,    // ví dụ: ACTIVE/CLOSED/OVERDUE...
                    "Trần B",
                    "Đang vay"
            );

            PledgeContractListResponse r2 = new PledgeContractListResponseImpl(
                    3L,
                    "CĐ252210-002",
                    LocalDate.now().minusDays(45),
                    LocalDate.now().minusDays(5),
                    "Trần Thị B",
                    "0909333777",
                    "Laptop Dell XPS 13",
                    new BigDecimal("18000000"),
                    new BigDecimal("6000000"),
                    new BigDecimal("12000000"),
                    LoanStatus.BINH_THUONG,
                    "Nguyễn C",
                    "Quá hạn"
            );

            List<PledgeContractListResponse> content = List.of(r1, r2);
            return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(new PageImpl<>(content, PageRequest.of(page, size), content.size())).getResponse();
        }

        // chế độ thật:
        return null;
    }
}