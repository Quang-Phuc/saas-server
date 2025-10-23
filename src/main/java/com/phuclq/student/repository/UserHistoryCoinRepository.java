package com.phuclq.student.repository;

import com.phuclq.student.domain.UserHistoryCoin;
import com.phuclq.student.dto.CategoryHomeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryCoinRepository extends JpaRepository<UserHistoryCoin, Integer> {

    UserHistoryCoin findAllByMrcOrderIdAndTxnIdIsNull(String mcrOrderId);

    List<UserHistoryCoin> findAllByCreatedBy(String createBy);

}
