package com.phuclq.student.repository;

import com.phuclq.student.domain.School;
import com.phuclq.student.domain.SchoolType;
import com.phuclq.student.dto.school.SchoolResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolTypeRepository extends JpaRepository<SchoolType, Long> {

List<SchoolType> findAllByType(String type);
}
