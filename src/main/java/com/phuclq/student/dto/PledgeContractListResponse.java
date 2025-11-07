package com.phuclq.student.dto;

import com.phuclq.student.types.LoanStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Giao diện projection dùng để hiển thị danh sách hợp đồng cầm đồ trong màn hình quản lý.
 * Dữ liệu được map trực tiếp từ native query (alias trong SQL phải trùng với tên getter).
 */
public interface PledgeContractListResponse {

    /** ID của hợp đồng cầm đồ */
    Long getId();

    /** Mã hợp đồng (ví dụ: CĐ252210-001) */
    String getContractCode();

    /** Ngày vay (ngày bắt đầu hợp đồng) */
    LocalDate getLoanDate();

    /** Ngày đến hạn trả (hạn cuối cùng khách hàng phải hoàn tất thanh toán) */
    LocalDate getDueDate();

    /** Tên khách hàng vay tiền */
    String getCustomerName();

    /** Số điện thoại khách hàng vay */
    String getPhoneNumber();

    /** Danh sách tên tài sản thế chấp (các asset được gộp bằng dấu phẩy) */
    String getAssetName();

    /** Số tiền vay ban đầu (gốc) */
    BigDecimal getLoanAmount();

    /** Tổng số tiền gốc mà khách hàng đã thanh toán (sum của principal_amount) */
    BigDecimal getTotalPaid();

    /** Số tiền gốc còn lại chưa thanh toán */
    BigDecimal getRemainingPrincipal();

    /** Trạng thái khoản vay: ACTIVE, CLOSED, OVERDUE... */
    LoanStatus getStatus();

    /** Nhân viên hiện đang phụ trách hợp đồng này */
    String getFollower();

    /** Trạng thái hợp đồng cầm đồ (pledgeStatus): ví dụ Đang vay, Quá hạn, Đã đóng */
    String getPledgeStatus();
}
