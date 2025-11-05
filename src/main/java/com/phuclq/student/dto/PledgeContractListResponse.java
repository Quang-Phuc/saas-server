package com.phuclq.student.dto;

import com.phuclq.student.types.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO dùng để hiển thị danh sách hợp đồng cầm đồ trong màn hình quản lý.
 * Bao gồm các thông tin cơ bản về khách hàng, tài sản, khoản vay và trạng thái hợp đồng.
 */
@Data
public class PledgeContractListResponse {

    /** ID của hợp đồng cầm đồ */
    private final Long id;

    /** Mã hợp đồng (ví dụ: CĐ252210-001) */
    private final String contractCode;

    /** Ngày vay (ngày bắt đầu hợp đồng) */
    private final LocalDate loanDate;

    /** Ngày đến hạn trả (ngày kết thúc hợp đồng hoặc hạn cuối trả nợ) */
    private final LocalDate dueDate;

    /** Tên khách hàng vay */
    private final String customerName;

    /** Số điện thoại khách hàng vay */
    private final String phoneNumber;

    /** Tên tài sản thế chấp (VD: Xe máy, ô tô, vàng...) */
    private final String assetName;

    /** Loại tài sản thế chấp (VD: Xe, Vàng, ĐTDĐ...) */
    private final String assetType;

    /** Số tiền vay ban đầu của hợp đồng */
    private final BigDecimal loanAmount;

    /** Tổng số tiền gốc + lãi mà khách hàng đã thanh toán */
    private final BigDecimal totalPaid;

    /** Số tiền gốc còn lại chưa thanh toán */
    private final BigDecimal remainingPrincipal;

    /** Trạng thái khoản vay (VD: ACTIVE, CLOSED, OVERDUE...) */
    private final LoanStatus status;

    /** Nhân viên đang phụ trách hợp đồng này */
    private final String follower;

    // Constructor
    public PledgeContractListResponse(Long id, String contractCode, LocalDate loanDate, LocalDate dueDate,
                                      String customerName, String phoneNumber, String assetName, String assetType,
                                      BigDecimal loanAmount, BigDecimal totalPaid, BigDecimal remainingPrincipal,
                                      LoanStatus status, String follower) {
        this.id = id;
        this.contractCode = contractCode;
        this.assetType = assetType;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.assetName = assetName;
        this.loanAmount = loanAmount;
        this.totalPaid = totalPaid;
        this.remainingPrincipal = remainingPrincipal;
        this.status = status;
        this.follower = follower;
    }

}
