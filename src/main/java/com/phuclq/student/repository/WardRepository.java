package com.phuclq.student.repository;

import com.phuclq.student.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {

    List<Ward> findAllByProvinceIdAndDistrictId(Integer provinceId, Integer districtId);
}
