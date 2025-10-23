package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Specialization;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.SpecializationRepository;
import com.phuclq.student.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
public class SpecialzationServiceImpl implements SpecializationService {
    @Autowired
    private SpecializationRepository specializationRepository;


    @Override
    public Page<Specialization> findAll(Pageable pageable, String search) {
        return !Objects.requireNonNull(search).isEmpty() ? specializationRepository.findAllByValueContainingIgnoreCase(search.trim(), pageable)
                : specializationRepository.findAll(pageable);
    }

    @Override
    public Optional<Specialization> findAllById(int id) {
        return specializationRepository.findById(id);
    }

    @Override
    public Specialization save(Specialization dto) {
        Specialization save = Objects.nonNull(dto.getId()) ? specializationRepository.findById(dto.getId()).get() : dto;
        if (Objects.nonNull(dto.getId())) {
            if (Objects.nonNull(dto.getValue())) {
                save.setIdUrl(getSearchableStringUrl(dto.getValue(), specializationRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getValue())).size()));
            }
        } else {
            if(Objects.nonNull(specializationRepository.findAllByValue(dto.getValue()))){
                throw new BusinessHandleException("SS024");
            }
            save.setIdUrl(getSearchableStringUrl(dto.getValue(), specializationRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getValue())).size()));

        }
        if (Objects.nonNull(dto.getValue())) {
            save.setValue(dto.getValue());
        }
        return specializationRepository.save(save);
    }

    @Override
    public Specialization update(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    @Override
    public void deleteById(int id) {
        specializationRepository.deleteById(id);
    }

    @Override
    public List<Specialization> findAllIndistry(Integer id) {
        return specializationRepository.findAllByIndustryId(id);
    }
}
