package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Industry;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.IndustryRepository;
import com.phuclq.student.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
public class IndustryServiceImpl implements IndustryService {
    @Autowired
    private IndustryRepository industryRepository;

    @Override
    public Page<Industry> findAll(Pageable pageable, String search) {
        return !Objects.requireNonNull(search).isEmpty() ? industryRepository.findAllByValueContainingIgnoreCase(search.trim(), pageable)
                : industryRepository.findAll(pageable);
    }

    @Override
    public Industry findAllById(int id) {
        return industryRepository.getOne(id);
    }

    @Override
    public Industry save(Industry dto) {

        Industry save = Objects.nonNull(dto.getId()) ? industryRepository.findAllById(dto.getId()) : dto;
        if (Objects.nonNull(dto.getId())) {
            if (Objects.nonNull(dto.getValue())) {
                save.setIdUrl(getSearchableStringUrl(dto.getValue(), industryRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getValue())).size()));
            }
        } else {
            if(Objects.nonNull(industryRepository.findAllByValue(dto.getValue()))){
                throw new BusinessHandleException("SS024");
            }
            dto.setIdUrl(getSearchableStringUrl(dto.getValue(), industryRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getValue())).size()));

        }
        if (Objects.nonNull(dto.getValue())) {
            save.setValue(dto.getValue());
        }
        return industryRepository.save(save);
    }

    @Override
    public Industry update(Industry industry) {
        return industryRepository.save(industry);
    }

    @Override
    public void deleteById(int id) {
        industryRepository.deleteById(id);

    }

    @Override
    public List<Industry> findAllIndustry() {
        return industryRepository.findAll();
    }
}
