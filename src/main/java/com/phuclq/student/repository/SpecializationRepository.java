package com.phuclq.student.repository;

import com.phuclq.student.domain.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {

    List<Specialization> findAllByIndustryId(Integer industryId);

    Page<Specialization> findAllByValueContainingIgnoreCase(String search, Pageable pageable);

    List<Specialization> findByIdUrlStartingWith(String id);

    Specialization findAllByValue(String value);
}
