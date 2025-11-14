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

        List<PaymentSchedule> schedules =
                paymentScheduleRepository.findByContractIdOrderByPeriodNumberAsc(contractId);

        if (schedules.isEmpty()) {
            throw new BusinessHandleException("SS901"); // Contract not found
        }

        boolean startAllocating = false;
        int currentIndex = 0;

        // Tìm kỳ bắt đầu
        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).getPeriodNumber().equals(req.getPeriodNumber())) {
                startAllocating = true;
                currentIndex = i;
                break;
            }
        }

        if (!startAllocating) {
            throw new BusinessHandleException("SS902"); // Kỳ không tồn tại
        }

        // === BƯỚC 1: THANH TOÁN TỪ KỲ HIỆN TẠI TRỞ ĐI ===
        for (int i = currentIndex; i < schedules.size() && remaining.compareTo(BigDecimal.ZERO) > 0; i++) {
            PaymentSchedule schedule = schedules.get(i);

            BigDecimal paidSoFar = schedule.getTransactions()
                    .stream()
                    .map(PaymentScheduleTransaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal need = schedule.getTotalAmount().subtract(paidSoFar);

            if (need.compareTo(BigDecimal.ZERO) <= 0) {
                schedule.setStatus("PAID");
                paymentScheduleRepository.save(schedule);
                continue;
            }

            if (remaining.compareTo(need) >= 0) {
                // Trả đủ kỳ
                createTransaction(schedule, need, req);
                remaining = remaining.subtract(need);
                schedule.setStatus("PAID");
            } else {
                // Trả một phần
                createTransaction(schedule, remaining, req);
                schedule.setStatus("PARTIAL");
                remaining = BigDecimal.ZERO;
            }
            paymentScheduleRepository.save(schedule);
        }

        // === BƯỚC 2: NẾU VẪN CÒN DƯ → TẠO GIAO DỊCH "DƯ TIỀN" CHO KỲ CUỐI ===
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            PaymentSchedule lastSchedule = schedules.get(schedules.size() - 1);

            // Tạo giao dịch dư (có thể có kỳ mới nếu cần)
            PaymentScheduleTransaction excessTx = new PaymentScheduleTransaction();
            excessTx.setSourceTransactionId(lastSchedule.getId());
            excessTx.setAmount(remaining);
            excessTx.setPaymentDate(req.getPayDate());
            excessTx.setPaymentMethod(req.getPaymentMethod());
            excessTx.setExcess(true);
            excessTx.setNote("Dư tiền từ kỳ " + req.getPeriodNumber() + " - áp dụng kỳ sau");

            // Lưu giao dịch dư
            transRepo.save(excessTx);

            // Cập nhật trạng thái kỳ cuối (nếu đã đủ thì vẫn PAID)
            BigDecimal totalPaid = lastSchedule.getTransactions()
                    .stream()
                    .map(PaymentScheduleTransaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalPaid.compareTo(lastSchedule.getTotalAmount()) >= 0) {
                lastSchedule.setStatus("PAID");
            } else {
                lastSchedule.setStatus("PARTIAL");
            }
            paymentScheduleRepository.save(lastSchedule);
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
