package com.phuclq.student.service;

import com.phuclq.student.domain.District;

import java.util.List;

public interface DistrictService {

    List<District> findAll();

    List<District> findAllByProvince(Integer provinceId);


}
