package com.phuclq.student.repository;

import com.phuclq.student.domain.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Integer> {


    Page<Industry> findAllByValueContainingIgnoreCase(String search, Pageable pageable);

    Industry findAllById(Integer id);

    Industry findAllByValue(String  value);

    List<Industry> findByIdUrlStartingWith(String id);

}
