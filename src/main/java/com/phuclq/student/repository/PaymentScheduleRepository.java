package com.phuclq.student.repository;
import com.phuclq.student.domain.PaymentSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {

    // Lấy tất cả lịch trả theo hợp đồng
    List<PaymentSchedule> findByContractId(Long contractId);

    Page<PaymentSchedule> findByContractId(Long contractId, Pageable pageable);

    // Lấy lịch trả theo trạng thái (ví dụ: PENDING, PAID)
    List<PaymentSchedule> findByContractIdAndStatus(Long contractId, String status);

    // Xóa tất cả lịch trả của 1 hợp đồng (nếu cần update lại)
    void deleteByContractId(Long contractId);

    List<PaymentSchedule> findByContractIdOrderByPeriodNumberAsc(Long contractId);

    Optional<PaymentSchedule> findByContractIdAndPeriodNumber(Long contractId, Integer periodNumber);

    // PaymentScheduleRepository.java
    List<PaymentSchedule> findByDueDateBeforeAndStatusNotIn(LocalDate dueDate, List<String> statuses);

    boolean existsByContractIdAndDueDateBeforeAndStatusNotIn(
            Long contractId, LocalDate dueDate, List<String> statuses);

    // PaymentScheduleRepository.java
    List<PaymentSchedule> findByDueDateBeforeAndStatusNot(LocalDate dueDate, String status);

    boolean existsByContractIdAndDueDateBeforeAndStatusIn(
            Long contractId, LocalDate dueDate, List<String> statuses);

    // PaymentScheduleRepository.java
    List<PaymentSchedule> findByDueDateLessThanEqual(LocalDate dueDate);

    @Query(
            "SELECT ps FROM PaymentSchedule ps " +
                    "LEFT JOIN ps.transactions tx " +
                    "WHERE ps.dueDate <= :today " +
                    "GROUP BY ps " +
                    "HAVING COALESCE(SUM(tx.amount), 0) < ps.totalAmount"
    )
    List<PaymentSchedule> findUnpaidSchedulesDueTodayOrBefore(@Param("today") LocalDate today);

}
