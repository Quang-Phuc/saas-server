package com.phuclq.student.repository;

import com.phuclq.student.domain.CategoryBLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryBlogRepository extends JpaRepository<CategoryBLog, Long> {

    Page<CategoryBLog> findAllByNameContainingIgnoreCase(String search, Pageable pageable);

    CategoryBLog findAllById(Long id);

    List<CategoryBLog> findByIdUrlStartingWith(String id);

    CategoryBLog findAllByName(String name);
}


