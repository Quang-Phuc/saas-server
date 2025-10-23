package com.phuclq.student.dto.rate;

import lombok.Data;

import java.util.Map;

@Data
public class RateDto {
    Map<Double, Long> rates;
    private Long total;
}
