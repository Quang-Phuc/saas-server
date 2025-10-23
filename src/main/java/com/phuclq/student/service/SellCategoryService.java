package com.phuclq.student.service;

import com.phuclq.student.domain.SellCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SellCategoryService {
    Page<SellCategory> findAll(Pageable pageable, String search);

    SellCategory findAllById(Long id);

    SellCategory save(SellCategory SellCategory);

    SellCategory update(SellCategory SellCategory);

    void deleteById(Long id);
}
