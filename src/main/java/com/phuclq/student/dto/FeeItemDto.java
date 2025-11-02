package com.phuclq.student.dto;

// (Đặt trong package ...dto)
import lombok.Data;
import java.math.BigDecimal;
@Data
public class FeeItemDto {
    private String type;
    private BigDecimal value;
}