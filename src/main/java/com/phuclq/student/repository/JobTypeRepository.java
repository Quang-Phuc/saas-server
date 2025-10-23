package com.phuclq.student.repository;

import com.phuclq.student.domain.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Long> {

    Page<JobType> findAllByNameContainingIgnoreCase(String search, Pageable pageable);


    List<JobType> findByIdUrlStartingWith(String id);

    JobType findAllById(Long id);

    JobType findAllByName(String name);
}
