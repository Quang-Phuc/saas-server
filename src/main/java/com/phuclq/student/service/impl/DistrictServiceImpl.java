package com.phuclq.student.service.impl;

import com.phuclq.student.domain.District;
import com.phuclq.student.repository.DistrictRepository;
import com.phuclq.student.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    DistrictRepository districtRepository;

    @Override
    public List<District> findAll() {
        return null;
    }

    @Override
    public List<District> findAllByProvince(Integer provinceId) {
        return districtRepository.findAllByProvinceId(provinceId);
    }
}
