package com.phuclq.student.service;

import com.phuclq.student.domain.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IndustryService {
    Page<Industry> findAll(Pageable pageable, String search);

    List<Industry> findAllIndustry();

    Industry findAllById(int id);

    Industry save(Industry industry);

    Industry update(Industry industry);

    void deleteById(int id);
}
