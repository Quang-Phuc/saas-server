package com.phuclq.student.repository;

import com.phuclq.student.domain.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Integer> {

    @Query(value = "select  t.id  from PAYMENT_REQUEST t where t.createdDate BETWEEN :startDate AND :endDate", nativeQuery = true)
    public List<PaymentRequest> getAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    PaymentRequest findAllByIdAndCreatedBy(Integer id, String createBy);

    PaymentRequest findAllById(Integer id);

}
