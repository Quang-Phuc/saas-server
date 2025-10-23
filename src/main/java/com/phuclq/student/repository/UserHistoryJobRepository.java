package com.phuclq.student.repository;

import com.phuclq.student.domain.UserHistoryJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryJobRepository extends JpaRepository<UserHistoryJob, Integer> {

    List<UserHistoryJob> findAllByCreatedBy(String createBy);

    UserHistoryJob findAllByCreatedByAndActivityIdAndJobIdAndType(String createdBy, Integer activityId, Long jobId, String type);


}
