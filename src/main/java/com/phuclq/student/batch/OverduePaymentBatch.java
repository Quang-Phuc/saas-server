// src/main/java/com/phuclq/student/batch/OverduePaymentBatch.java
package com.phuclq.student.batch;

import com.phuclq.student.domain.Loan;
import com.phuclq.student.domain.PaymentSchedule;
import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.repository.LoanRepository;
import com.phuclq.student.repository.PaymentScheduleRepository;
import com.phuclq.student.repository.PledgeContractRepository;
import com.phuclq.student.types.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OverduePaymentBatch {

    private final PaymentScheduleRepository paymentScheduleRepository;
    private final PledgeContractRepository pledgeContractRepository;
    private final LoanRepository loanRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void processOverduePayments() {
        LocalDate today = LocalDate.now();
        log.info("Bắt đầu tính phạt quá hạn - {}", today);

        // 1. LẤY TẤT CẢ KỲ ĐẾN HẠN TRƯỚC HÔM NAY
        List<PaymentSchedule> overdueSchedules = paymentScheduleRepository.findUnpaidSchedulesDueTodayOrBefore(today);

        for (PaymentSchedule ps : overdueSchedules) {
            long overdueDays = ChronoUnit.DAYS.between(ps.getDueDate(), today);

            // 2. PHẠT = totalAmount × 0.5% × số ngày quá hạn
            BigDecimal penaltyRate = BigDecimal.valueOf(0.005); // 0.5%
            BigDecimal dailyPenalty = ps.getTotalAmount().multiply(penaltyRate);
            BigDecimal penalty = dailyPenalty.multiply(BigDecimal.valueOf(overdueDays)).setScale(0, RoundingMode.HALF_UP);

            // 3. CẬP NHẬT
            ps.setOverdueDays((int) overdueDays);
            ps.setPenaltyInterest(penalty);
            ps.setStatus("OVERDUE");

            paymentScheduleRepository.save(ps);

            log.info("Kỳ {} - HĐ {}: Quá hạn {} ngày → Phạt {}đ (tổng kỳ: {})", ps.getPeriodNumber(), ps.getContractId(), overdueDays, penalty, ps.getTotalAmount());

            updateLoanStatusIfNeeded(ps.getContractId(),overdueDays);
        }

        log.info("Hoàn tất - {} kỳ bị phạt", overdueSchedules.size());
    }

    /**
     * Cập nhật trạng thái Loan:
     * - Quá hạn 1-59 ngày → RISKY
     * - Quá hạn ≥ 60 ngày → BAD_DEBT
     * - Không quá hạn → NORMAL_2
     */
    private void updateLoanStatusIfNeeded(Long contractId, long maxOverdueDays) {
        PledgeContract contract = pledgeContractRepository.findById(contractId).orElse(null);
        if (contract == null || contract.getLoanId() == null) return;

        Loan loan = loanRepository.findById(contract.getLoanId()).orElse(null);
        if (loan == null) return;

        LoanStatus newStatus;
        if (maxOverdueDays >= 60) {
            newStatus = LoanStatus.BAD_DEBT;
        } else if (maxOverdueDays > 0) {
            newStatus = LoanStatus.RISKY;
        } else {
            newStatus = LoanStatus.NORMAL_2;
        }

        if (loan.getStatus() != newStatus) {
            loan.setStatus(newStatus);
            loanRepository.save(loan);
            log.info("Cập nhật Loan {} → {}", loan.getId(), newStatus);
        }
    }
}