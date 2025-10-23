package com.phuclq.student.repository;

import com.phuclq.student.domain.UserHistoryHome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryHomeRepository extends JpaRepository<UserHistoryHome, Long> {

    List<UserHistoryHome> findAllByCreatedBy(String createBy);

    UserHistoryHome findAllByCreatedByAndActivityIdAndHomeId(String createBy, Integer activity, Long homeId);

    List<UserHistoryHome> findAllByCreatedByAndActivityIdAndHomeIdIn(String createBy, Integer activity, List<Long> homeId);

    List<UserHistoryHome> findAllByActivityIdAndHomeIdIn(Integer activity, List<Long> homeId);


}
