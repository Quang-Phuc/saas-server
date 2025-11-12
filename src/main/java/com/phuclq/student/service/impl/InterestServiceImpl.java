package com.phuclq.student.service.impl;

import com.phuclq.student.domain.PaymentSchedule;
import com.phuclq.student.repository.PaymentScheduleRepository;
import com.phuclq.student.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    @Override
    public Page<PaymentSchedule> getDetails(Long pledgeId, int page, int size, String sort, String order) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortBy = Sort.by(direction, sort != null ? sort : "periodNumber");

        Pageable pageable = PageRequest.of(page, size, sortBy);

        return  paymentScheduleRepository.findByContractId(pledgeId, pageable);

    }
}
