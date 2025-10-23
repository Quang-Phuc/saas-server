package com.phuclq.student.service;

import com.phuclq.student.domain.Rate;

import java.util.Map;

public interface RateService {

    Rate rate(Rate rate);


    Map<Double, Long> rateByIdAndType(String id, String type);
}
