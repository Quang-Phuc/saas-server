package com.phuclq.student.dto;


import com.phuclq.student.types.LoanStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PledgeSearchRequest {
    private String keyword; // Tên khách hàng, số điện thoại, mã hợp đồng
    private LoanStatus status; // Trạng thái khoản vay
    private String storeId; // Cửa hàng
    private String assetType; // Loại tài sản
    private LocalDate fromDate; // Ngày vay từ
    private LocalDate toDate; // Ngày vay đến
    private int page = 0; // Trang hiện tại
    private int size = 10; // Số record/trang
}
