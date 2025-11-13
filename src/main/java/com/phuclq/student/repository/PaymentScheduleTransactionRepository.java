package com.phuclq.student.repository;

import com.phuclq.student.domain.PaymentScheduleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleTransactionRepository
        extends JpaRepository<PaymentScheduleTransaction, Long> {

    List<PaymentScheduleTransaction> findByPaymentScheduleId(Long scheduleId);

}

