package com.phuclq.student.repository;

import com.phuclq.student.domain.SellCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellCategoryRepository extends JpaRepository<SellCategory, Long> {

    Page<SellCategory> findAllByNameContainingIgnoreCase(String trim, Pageable pageable);

    List<SellCategory> findByIdUrlStartingWith(String id);

    SellCategory findAllByName(String name);
}
