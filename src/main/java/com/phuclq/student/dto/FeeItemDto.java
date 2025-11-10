package com.phuclq.student.dto;

// (Đặt trong package ...dto)
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeItemDto {
    private String type;
    private BigDecimal value;
}