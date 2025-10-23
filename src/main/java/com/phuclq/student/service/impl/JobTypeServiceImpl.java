package com.phuclq.student.service.impl;

import com.phuclq.student.domain.JobType;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.JobTypeRepository;
import com.phuclq.student.service.JobTypeService;
import com.phuclq.student.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
@RequiredArgsConstructor
public class JobTypeServiceImpl implements JobTypeService {

    private final JobTypeRepository jobTypeRepository;

    private final UserService userService;

    @Override
    public Page<JobType> findAll(Pageable pageable, String search) {
        return Objects.nonNull(search) ? jobTypeRepository.findAllByNameContainingIgnoreCase(search.trim(), pageable)
                : jobTypeRepository.findAll(pageable);
    }

    @Override
    public JobType findAllById(Long id) {
        return jobTypeRepository.getOne(id);
    }

    @Override
    public JobType save(JobType dto) {

        JobType save = Objects.nonNull(dto.getId()) ? jobTypeRepository.findAllById(dto.getId()) : dto;
        if (Objects.nonNull(dto.getId())) {
            if (Objects.nonNull(dto.getName())) {
                save.setIdUrl(getSearchableStringUrl(dto.getName(), jobTypeRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));
            }
        } else {
            if(Objects.nonNull(jobTypeRepository.findAllByName(dto.getName()))){
                throw new BusinessHandleException("SS024");
            }
            dto.setIdUrl(getSearchableStringUrl(dto.getName(), jobTypeRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));

        }
        if (Objects.nonNull(dto.getName())) {
            save.setName(dto.getName());
        }
        return jobTypeRepository.save(save);
    }

    @Override
    public JobType update(JobType industry) {
        return jobTypeRepository.save(industry);
    }

    @Override
    public void deleteById(Long id) {
        jobTypeRepository.deleteById(id);

    }
}
