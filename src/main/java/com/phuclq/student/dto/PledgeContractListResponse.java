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

    // === MỚI: TỔNG LÃI ===
    /** Tổng tiền lãi phải trả (từ tất cả các kỳ trong payment_schedule) */
    BigDecimal getTotalInterest();

    // === MỚI: TỔNG PHÍ KHO ===
    /** Tổng phí kho (từ warehouse_daily_fee trong payment_schedule) */
    BigDecimal getTotalWarehouseFee();

    // === MỚI: TỔNG PHÍ DỊCH VỤ ===
    /** Tổng phí dịch vụ (tính từ fee_details: % hoặc cố định) */
    BigDecimal getTotalServiceFee();

    // === MỚI: TỔNG PHẢI THU ===
    /** Tổng số tiền khách phải trả = vay + lãi + kho + phí */
    BigDecimal getTotalReceivable();

    // === MỚI: TỔNG ĐÃ THU ===
    /** Tổng số tiền đã thanh toán (gốc + lãi + kho) */
    BigDecimal getTotalPaid();

    // === MỚI: CÒN NỢ ===
    /** Số tiền còn lại chưa thanh toán (totalReceivable - totalPaid) */
    BigDecimal getRemainingAmount();

    BigDecimal getTotalPenaltyInterest();

    // === CŨ: ĐÃ ĐƯỢC THAY THẾ BỞI remainingAmount ===
    // @Deprecated
    // BigDecimal getRemainingPrincipal();

    /** Trạng thái khoản vay: ACTIVE, CLOSED, OVERDUE... */
    LoanStatus getStatus();

    /** Nhân viên hiện đang phụ trách hợp đồng này */
    String getFollower();

    /** Trạng thái hợp đồng cầm đồ: DANG_VAY, QUA_HAN, DA_TRA_HET, DA_DONG */
    String getPledgeStatus();
}