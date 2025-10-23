package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Ward;
import com.phuclq.student.repository.WardRepository;
import com.phuclq.student.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardServiceImpl implements WardService {

    @Autowired
    WardRepository wardRepository;

    @Override
    public List<Ward> findAll() {
        return null;
    }

    @Override
    public List<Ward> findAllByProvinceAndDistrict(Integer provinceId, Integer districtId) {
        return wardRepository.findAllByProvinceIdAndDistrictId(provinceId,
                districtId);
    }
}
