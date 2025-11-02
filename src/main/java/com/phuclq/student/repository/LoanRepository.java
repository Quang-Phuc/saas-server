package com.phuclq.student.repository;

// (Đặt trong package ...repository)
import com.phuclq.student.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}