package com.phuclq.student.service;

import com.phuclq.student.domain.Category;
import com.phuclq.student.dto.CategoryFileDTO;
import com.phuclq.student.dto.CategoryHomeDTO;
import com.phuclq.student.dto.FileByCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable, String search);

    Category findAllById(Long id);

    Category save(Category category);

    Category update(Category category);

    void deleteById(Long id);

    List<CategoryHomeDTO> getCategoriesHome();

    Page<CategoryFileDTO> findFileFromCategories(Pageable pageable);

    FileByCategoryDto fileFromCategorie(Long id, Pageable pageable);

}
