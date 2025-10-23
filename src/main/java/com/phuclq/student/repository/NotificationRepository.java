package com.phuclq.student.repository;

import com.phuclq.student.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByAssigneeOrderByIdDesc(String userId, Pageable pageable);

    Notification findAllByAssigneeAndAndId(String userId, Long id);

}

