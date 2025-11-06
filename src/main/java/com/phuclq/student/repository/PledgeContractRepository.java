package com.phuclq.student.repository;

import com.phuclq.student.dto.PledgeContractListResponse;
import com.phuclq.student.types.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.phuclq.student.domain.PledgeContract;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface PledgeContractRepository extends JpaRepository<PledgeContract, Long> {

    @Query(
            value = "SELECT " +
                    "pc.id AS id, " +
                    "pc.contract_code AS contractCode, " +
                    "l.loan_date AS loanDate, " +
                    "l.due_date AS dueDate, " +
                    "c.full_name AS customerName, " +
                    "c.phone_number AS phoneNumber, " +
                    "GROUP_CONCAT(DISTINCT ca.asset_name SEPARATOR ', ') AS assetName, " +
                    "l.loan_amount AS loanAmount, " +
                    "COALESCE(SUM(ps.principal_amount), 0) AS totalPaid, " +
                    "l.remaining_principal AS remainingPrincipal, " +
                    "CASE " +
                    "   WHEN SUM(CASE WHEN ps.status = 'PAID' THEN 1 ELSE 0 END) = COUNT(ps.id) THEN 'CLOSED' " +
                    "   WHEN SUM(CASE WHEN ps.due_date < CURRENT_DATE AND ps.status <> 'PAID' THEN 1 ELSE 0 END) > 0 THEN 'OVERDUE' " +
                    "   ELSE 'ACTIVE' " +
                    "END AS status, " +
                    "pc.follower AS follower " +
                    "FROM pledge_contract pc " +
                    "JOIN loan l ON pc.loan_id = l.id " +
                    "JOIN customer c ON pc.customer_id = c.id " +
                    "LEFT JOIN collateral_asset ca ON ca.contract_id = pc.id " +
                    "LEFT JOIN payment_schedule ps ON ps.contract_id = pc.id " +
                    "WHERE (:keyword IS NULL OR LOWER(c.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "   OR c.phone_number LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (:status IS NULL OR " +
                    "     (CASE " +
                    "         WHEN SUM(CASE WHEN ps.status = 'PAID' THEN 1 ELSE 0 END) = COUNT(ps.id) THEN 'CLOSED' " +
                    "         WHEN SUM(CASE WHEN ps.due_date < CURRENT_DATE AND ps.status <> 'PAID' THEN 1 ELSE 0 END) > 0 THEN 'OVERDUE' " +
                    "         ELSE 'ACTIVE' " +
                    "      END) = :status) " +
                    "AND (:fromDate IS NULL OR l.loan_date >= :fromDate) " +
                    "AND (:toDate IS NULL OR l.loan_date <= :toDate) " +
                    "AND (:follower IS NULL OR pc.follower = :follower) " +
                    "GROUP BY pc.id, pc.contract_code, l.loan_date, l.due_date, c.full_name, c.phone_number, " +
                    "l.loan_amount, l.remaining_principal, pc.follower " +
                    "ORDER BY pc.id DESC",
            countQuery = "SELECT COUNT(DISTINCT pc.id) " +
                    "FROM pledge_contract pc " +
                    "JOIN loan l ON pc.loan_id = l.id " +
                    "JOIN customer c ON pc.customer_id = c.id " +
                    "LEFT JOIN payment_schedule ps ON ps.contract_id = pc.id " +
                    "WHERE (:keyword IS NULL OR LOWER(c.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "   OR c.phone_number LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (:fromDate IS NULL OR l.loan_date >= :fromDate) " +
                    "AND (:toDate IS NULL OR l.loan_date <= :toDate) " +
                    "AND (:follower IS NULL OR pc.follower = :follower)",
            nativeQuery = true
    )
    Page<Object[]> searchContracts(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("follower") String follower,
            Pageable pageable
    );

    @Query("SELECT COUNT(p) FROM PledgeContract p WHERE p.createdDate >= :start AND p.createdDate < :end")
    Long countByCreatedDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
