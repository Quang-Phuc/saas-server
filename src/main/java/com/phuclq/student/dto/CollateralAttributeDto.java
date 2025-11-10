package com.phuclq.student.dto;

import lombok.Data;

/**
 * DTO đại diện cho thuộc tính của tài sản thế chấp (CollateralAttribute).
 * Mỗi attribute là 1 cặp label - value, ví dụ:
 *   "Biển số" : "29A1-12345"
 */
@Data
public class CollateralAttributeDto {

    /** Tên thuộc tính hiển thị (ví dụ: "Màu xe", "Số khung", "Biển số") */
    private String label;

    /** Giá trị của thuộc tính (ví dụ: "Đen", "12345AB", "29A1-12345") */
    private String value;

    /** Thuộc tính này có bắt buộc nhập hay không */
    private Boolean required;
}
