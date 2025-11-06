package com.phuclq.student.service;

import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.dto.PledgeContractDetailResponse;
import com.phuclq.student.dto.PledgeContractListResponse;
import com.phuclq.student.types.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface PledgeContractService {

    PledgeContract createPledge(
            String payloadJson,
            MultipartFile portraitFile,
            List<MultipartFile> attachmentFiles
    );

    public PledgeContractDetailResponse getPledgeDetail(Long id) ;

    /**
     * Tìm kiếm danh sách hợp đồng cầm đồ có phân trang & lọc.
     *
     * @param keyword  Tên hoặc số điện thoại khách hàng
     * @param status   Trạng thái hợp đồng (ACTIVE, OVERDUE, CLOSED)
     * @param fromDate Ngày bắt đầu lọc
     * @param toDate   Ngày kết thúc lọc
     * @param followerId Nhân viên phụ trách
     * @param pageable Thông tin phân trang
     * @return Trang danh sách hợp đồng
     */
    Page<PledgeContractListResponse> searchContracts(
            String keyword,
            LoanStatus loanStatus,
            String status,
            LocalDate fromDate,
            LocalDate toDate,
            Long followerId,
            Pageable pageable
    );
}