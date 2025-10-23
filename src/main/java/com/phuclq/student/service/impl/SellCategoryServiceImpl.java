package com.phuclq.student.service.impl;

import com.phuclq.student.domain.SellCategory;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.SellCategoryRepository;
import com.phuclq.student.service.SellCategoryService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
public class SellCategoryServiceImpl implements SellCategoryService {

    @Autowired
    private SellCategoryRepository sellCategoryRepository;
    @Autowired
    private UserService userService;

    @Override
    public Page<SellCategory> findAll(Pageable pageable, String search) {
        return !Objects.requireNonNull(search).isEmpty() ? sellCategoryRepository.findAllByNameContainingIgnoreCase(search.trim(), pageable)
                : sellCategoryRepository.findAll(pageable);
    }

    @Override
    public SellCategory findAllById(Long id) {
        return sellCategoryRepository.getOne(id);
    }

    @Override
    public SellCategory save(SellCategory dto) {

        SellCategory save = Objects.nonNull(dto.getId()) ? sellCategoryRepository.findById(dto.getId()).get() : dto;
        if (Objects.nonNull(dto.getId())) {
            if (Objects.nonNull(dto.getName())) {
                save.setIdUrl(getSearchableStringUrl(dto.getName(), sellCategoryRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));
            }
        } else {
            if(Objects.nonNull(sellCategoryRepository.findAllByName(dto.getName()))){
                throw new BusinessHandleException("SS024");
            }
            save.setIdUrl(getSearchableStringUrl(dto.getName(), sellCategoryRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));

        }

        if (Objects.nonNull(dto.getName())) {
            save.setName(dto.getName());
        }
        return sellCategoryRepository.save(save);

    }

    @Override
    public SellCategory update(SellCategory industry) {
        return sellCategoryRepository.save(industry);
    }

    @Override
    public void deleteById(Long id) {
        sellCategoryRepository.deleteById(id);

    }
}
