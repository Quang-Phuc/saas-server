package com.phuclq.student.repository;

import com.phuclq.student.domain.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CVRepository extends JpaRepository<CV, Long> {

    CV findAllByIdAndCreatedBy(Long id, String userId);

}
