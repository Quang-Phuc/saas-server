package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Province;
import com.phuclq.student.dto.AddressResult;
import com.phuclq.student.repository.ProvinceRepository;
import com.phuclq.student.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    ProvinceRepository provinceRepository;

    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Optional<Province> findAllById(Integer id) {
        return provinceRepository.findById(id);
    }

    @Override
    public List<AddressResult> findAll(String search) {

        return Objects.nonNull(search) && !search.isEmpty() ? provinceRepository.findAddressBySearch(search.trim().toUpperCase()) : provinceRepository.findAddress();

    }

    @Override
    public Province save(Province province) {
        return provinceRepository.save(province);
    }

    @Override
    public void deleteById(int id) {
        provinceRepository.deleteById(id);
    }


    @Override
    public Province update(Province province) {


        return null;
    }
}
