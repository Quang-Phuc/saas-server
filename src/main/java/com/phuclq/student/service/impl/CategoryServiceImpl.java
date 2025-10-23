package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Category;
import com.phuclq.student.domain.File;
import com.phuclq.student.dto.*;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.CategoryRepository;
import com.phuclq.student.repository.FileRepository;
import com.phuclq.student.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    FileRepository fileRepository;


    @Override
    public Page<Category> findAll(Pageable pageable, String search) {

        return !Objects.requireNonNull(search).isEmpty() ? categoryRepository.findAllByCategoryContainingIgnoreCase(search.trim(), pageable)
                : categoryRepository.findAll(pageable);
    }

    @Override
    public Category findAllById(Long id) {
        return categoryRepository.findAllById(id);
    }

    @Override
    public Category save(Category category) {
        Category save = Objects.nonNull(category.getId()) ? categoryRepository.findAllById(category.getId()) : category;
        if (Objects.nonNull(category.getId())) {
            if (Objects.nonNull(category.getCategory())) {
                save.setIdUrl(getSearchableStringUrl(category.getCategory(), categoryRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(category.getCategory())).size()));
            }
        } else {
            if(Objects.nonNull(categoryRepository.findAllByCategory(category.getCategory()))){
                throw new BusinessHandleException("SS024");
            }
            category.setIdUrl(getSearchableStringUrl(category.getCategory(), categoryRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(category.getCategory())).size()));

        }
        if (Objects.nonNull(category.getCategory())) {
            save.setCategory(category.getCategory());
        }
        return categoryRepository.save(save);
    }


    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryHomeDTO> getCategoriesHome() {
        List<CategoryHomeDTO> categoryHomeDTOList = categoryRepository.getCategoriesHome().stream().map(CategoryHomeDTO::new).collect(Collectors.toList());
        return categoryHomeDTOList;
    }

    @Override
    public Category update(Category category) {

        Category CategoryById = findAllById(category.getId());
        CategoryById.setCategory(category.getCategory());

        return categoryRepository.save(CategoryById);
    }

    @Override
    public Page<CategoryFileDTO> findFileFromCategories(Pageable pageable) {


        List<CategoryFileDTO> listCatelogyDTO = new ArrayList<CategoryFileDTO>();
        List<Category> catelogyList = categoryRepository.findAll();

        catelogyList.forEach(category -> {
            CategoryFileDTO categoryFileDTO = new CategoryFileDTO();

            categoryFileDTO.setId(category.getId());
            categoryFileDTO.setNameCategory(category.getCategory());

            List<File> fileList = fileRepository.findAllByCategoryId(category.getId());
            List<FileDTO> fileDTOS = fileList.stream().map(FileDTO::new).collect(Collectors.toList());
            categoryFileDTO.setFileDTOList(fileDTOS);

            listCatelogyDTO.add(categoryFileDTO);
        });

        Page<CategoryFileDTO> pagesCatelogy = new PageImpl<CategoryFileDTO>(listCatelogyDTO, pageable, listCatelogyDTO.size());
        return pagesCatelogy;
    }

    @Override
    public FileByCategoryDto fileFromCategorie(Long id, Pageable pageable) {
        Category category = categoryRepository.findById(id).get();
        FileByCategoryDto files = new FileByCategoryDto();
        Page<FileResultInterface> page = fileRepository.findByCategoryId(id, pageable);
        files.setCategory(category.getCategory());
        files.setId(category.getId());
        files.setListFile(page.getContent());
        return files;
    }
}

