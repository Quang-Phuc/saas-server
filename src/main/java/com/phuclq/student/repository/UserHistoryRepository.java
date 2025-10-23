package com.phuclq.student.repository;

import com.phuclq.student.domain.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Integer> {
    Page<UserHistory> findUserHistoriesByUserId(Integer userId, Pageable pageable);

    Page<UserHistory> findUserHistoriesByUserIdAndActivityId(Integer userId, Integer activityId, Pageable pageable);

    List<UserHistory> findByOrderId(String orderId);

    @Query(value = "select * from user_history where status = 2 and user_id=?1 and activity_id=1", nativeQuery = true)
    List<UserHistory> findDownloadUserHistory(Integer userId, Pageable pageable);

}
