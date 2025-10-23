package com.phuclq.student.repository;

import com.phuclq.student.domain.UserHistoryBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryBlogRepository extends JpaRepository<UserHistoryBlog, Long> {

    List<UserHistoryBlog> findAllByCreatedBy(String createBy);


    UserHistoryBlog findAllByCreatedByAndActivityIdAndBlogId(String createBy, Integer activity, Long homeId);

    List<UserHistoryBlog> findAllByCreatedByAndActivityIdAndBlogIdIn(String createBy, Integer activity, List<Long> homeId);

    List<UserHistoryBlog> findAllByActivityIdAndBlogIdIn(Integer activity, List<Long> homeId);

    List<UserHistoryBlog> findAllByActivityIdAndBlogIdInAndCreatedBy(Integer activity, List<Long> homeId, String createdBy);


}
