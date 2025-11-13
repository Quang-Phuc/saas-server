package com.phuclq.student.service.impl;

import com.phuclq.student.domain.PaymentSchedule;
import com.phuclq.student.domain.PaymentScheduleTransaction;
import com.phuclq.student.dto.PayInterestRequest;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.PaymentScheduleRepository;
import com.phuclq.student.repository.PaymentScheduleTransactionRepository;
import com.phuclq.student.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final PaymentScheduleRepository paymentScheduleRepository;
    private final PaymentScheduleTransactionRepository transRepo;

    @Override
    public Page<PaymentSchedule> getDetails(Long pledgeId, int page, int size, String sort, String order) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(direction, sort != null ? sort : "periodNumber");

        Pageable pageable = PageRequest.of(page, size, sortBy);

        return  paymentScheduleRepository.findByContractId(pledgeId, pageable);

    }

    @Override
    public void payInterest(Long contractId, PayInterestRequest req) {

        BigDecimal remaining = req.getAmount();
        if (remaining == null || remaining.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessHandleException("SS900"); // Invalid amount
        }

        // Lấy danh sách kỳ theo thứ tự
        List<PaymentSchedule> schedules =
                paymentScheduleRepository.findByContractIdOrderByPeriodNumberAsc(contractId);

        if (schedules.isEmpty()) {
            throw new BusinessHandleException("SS901"); // Contract not found
        }

        // Bắt đầu từ kỳ FE truyền (periodNumber)
        boolean startAllocating = false;

        for (PaymentSchedule schedule : schedules) {

            if (!startAllocating) {
                if (schedule.getPeriodNumber().equals(req.getPeriodNumber())) {
                    startAllocating = true;
                } else {
                    continue;
                }
            }

            BigDecimal paidSoFar = schedule.getTransactions()
                    .stream()
                    .map(PaymentScheduleTransaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal need = schedule.getTotalAmount().subtract(paidSoFar);

            // đã trả đủ rồi → bỏ qua
            if (need.compareTo(BigDecimal.ZERO) <= 0) {
                schedule.setStatus("PAID");
                paymentScheduleRepository.save(schedule);
                continue;
            }

            // Nếu tiền còn lại đủ trả trọn kỳ
            if (remaining.compareTo(need) >= 0) {
                createTransaction(schedule, need, req);
                remaining = remaining.subtract(need);

                schedule.setStatus("PAID");
                paymentScheduleRepository.save(schedule);
            }
            else {
                // Chỉ trả được một phần
                createTransaction(schedule, remaining, req);
                schedule.setStatus("PARTIAL");
                paymentScheduleRepository.save(schedule);

                remaining = BigDecimal.ZERO;
                break;
            }
        }

        // Nếu hết kỳ rồi vẫn dư tiền → tùy bạn xử lý
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            // Có thể tạo record trong bảng dư tiền cuối cùng
            // Hoặc bỏ qua, tùy nghiệp vụ.
        }
    }

    private void createTransaction(PaymentSchedule schedule, BigDecimal amount, PayInterestRequest req) {

        PaymentScheduleTransaction trans = PaymentScheduleTransaction.builder()
                .paymentScheduleId(schedule.getId())
                .amount(amount)
                .paymentDate(req.getPayDate())
                .paymentMethod(req.getPaymentMethod())
                .type("INTEREST") // mặc định
                .note(req.getNote())
                .build();

        transRepo.save(trans);
    }
}
