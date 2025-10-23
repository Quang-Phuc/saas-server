package com.phuclq.student.service;

import com.phuclq.student.domain.School;
import com.phuclq.student.domain.SchoolType;
import com.phuclq.student.dto.SchoolResultDto;
import com.phuclq.student.dto.school.SchoolRequest;
import com.phuclq.student.dto.school.SchoolResultDetail;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface SchoolService {
    List<School> findAll();

    SchoolResultDetail findAllById(String idUrl) throws IOException;

    School saveOrUpdate(SchoolRequest school) throws IOException;

    void deleteById(Long id);

    List<School> saveSchools();

    SchoolResultDto findAll(Pageable pageable, SchoolRequest search);

    List<SchoolType> getSchoolType(String type);

}
