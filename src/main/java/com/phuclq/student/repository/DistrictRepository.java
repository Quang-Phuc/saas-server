package com.phuclq.student.repository;

import com.phuclq.student.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {

    List<District> findAllByProvinceId(Integer provinceId);
}
