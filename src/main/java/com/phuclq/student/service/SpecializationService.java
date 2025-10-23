package com.phuclq.student.service;

import com.phuclq.student.domain.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SpecializationService {
    Page<Specialization> findAll(Pageable pageable, String search);

    Optional<Specialization> findAllById(int id);

    Specialization save(Specialization specialization);

    Specialization update(Specialization specialization);

    void deleteById(int id);

    List<Specialization> findAllIndistry(Integer id);
}
