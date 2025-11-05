package com.phuclq.student.repository;

import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.dto.PledgeContractListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PledgeContractRepository extends JpaRepository<PledgeContract, Long> {

//    @Query("SELECT new com.phuclq.student.dto.PledgeContractListResponse( " +
//            "pc.id, " +
//            "l.contractCode, " +
//            "l.loanDate, " +
//            "l.dueDate, " +
//            "c.fullName, " +
//            "c.phoneNumber, " +
//            "ca.assetName, " +
//            "ca.assetType, " +
//            "l.loanAmount, " +
//            "COALESCE(SUM(ps.principalAmount), 0), " +
//            "l.remainingPrincipal, " +
//            "l.status, " +
//            "l.follower) " +
//            "FROM PledgeContract pc " +
//            "JOIN Loan l ON pc.loanId = l.id " +
//            "JOIN Customer c ON pc.customerId = c.id " +
//            "LEFT JOIN CollateralAsset ca ON pc.collateralId = ca.id " +
//            "LEFT JOIN PaymentSchedule ps ON ps.contractId = pc.id AND ps.status = 'PAID' " +
//            "WHERE (:keyword IS NULL " +
//            "   OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "   OR c.phoneNumber LIKE CONCAT('%', :keyword, '%') " +
//            "   OR l.contractCode LIKE CONCAT('%', :keyword, '%')) " +
//            "AND (:status IS NULL OR l.status = :status) " +
//            "AND (:storeId IS NULL OR pc.storeId = :storeId) " +
//            "AND (:fromDate IS NULL OR l.loanDate >= :fromDate) " +
//            "AND (:toDate IS NULL OR l.loanDate <= :toDate) " +
//            "AND (:follower IS NULL OR l.follower = :follower) " +
//            "GROUP BY pc.id, l.contractCode, l.loanDate, l.dueDate, c.fullName, c.phoneNumber, " +
//            "ca.assetName, ca.assetType, l.loanAmount, l.remainingPrincipal, l.status, l.follower " +
//            "ORDER BY l.loanDate DESC")
//    List<PledgeContractListResponse> searchPledgeContracts(
//            @Param("keyword") String keyword,
//            @Param("status") String status,
//            @Param("storeId") Long storeId,
//            @Param("fromDate") LocalDate fromDate,
//            @Param("toDate") LocalDate toDate,
//            @Param("follower") String follower
//    );


}
