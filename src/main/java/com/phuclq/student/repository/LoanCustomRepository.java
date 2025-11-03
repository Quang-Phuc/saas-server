package com.phuclq.student.repository;

import com.phuclq.student.domain.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface LoanCustomRepository {
    Page<Loan> searchLoans(String keyword, String status, LocalDate fromDate,
                           LocalDate toDate, Long employeeId, Pageable pageable);
}
