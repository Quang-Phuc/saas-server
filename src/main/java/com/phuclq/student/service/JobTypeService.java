package com.phuclq.student.service;

import com.phuclq.student.domain.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobTypeService {
    Page<JobType> findAll(Pageable pageable, String search);

    JobType findAllById(Long id);

    JobType save(JobType JobType);

    JobType update(JobType JobType);

    void deleteById(Long id);
}
