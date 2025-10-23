package com.phuclq.student.service;

import com.phuclq.student.domain.Ward;

import java.util.List;

public interface WardService {

    List<Ward> findAll();

    List<Ward> findAllByProvinceAndDistrict(Integer provinceId, Integer districtId);


}
