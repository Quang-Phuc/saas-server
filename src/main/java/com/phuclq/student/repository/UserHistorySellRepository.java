package com.phuclq.student.repository;

import com.phuclq.student.domain.UserHistorySell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistorySellRepository extends JpaRepository<UserHistorySell, Long> {

    List<UserHistorySell> findAllByCreatedBy(String createBy);


    UserHistorySell findAllByCreatedByAndActivityIdAndSellId(String createBy, Integer activity, Long homeId);

    List<UserHistorySell> findAllByCreatedByAndActivityIdAndSellIdIn(String createBy, Integer activity, List<Long> homeId);


    List<UserHistorySell> findAllByActivityIdAndSellIdIn(Integer activity, List<Long> homeId);

    List<UserHistorySell> findAllByActivityIdAndSellIdInAndCreatedBy(Integer activity, List<Long> homeId, String createdBy);


}
