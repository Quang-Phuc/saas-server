package com.phuclq.student.service;

import com.phuclq.student.domain.PaymentSchedule;
import com.phuclq.student.dto.PayInterestRequest;
import org.springframework.data.domain.Page;

public interface InterestService {

    public Page<PaymentSchedule> getDetails(Long pledgeId, int page, int size, String sort, String order);

    void payInterest(Long contractId, PayInterestRequest request);
}
