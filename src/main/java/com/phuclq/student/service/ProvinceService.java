package com.phuclq.student.service;

import com.phuclq.student.domain.Province;
import com.phuclq.student.dto.AddressResult;

import java.util.List;
import java.util.Optional;

public interface ProvinceService {
    List<Province> findAll();

    Optional<Province> findAllById(Integer id);

    List<AddressResult> findAll(String search);

    Province save(Province province);

    Province update(Province province);

    void deleteById(int id);

}
