package com.phuclq.student.service.impl;

import com.phuclq.student.domain.RentalHouse;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.RentalHouseDto;
import com.phuclq.student.dto.RentalHouseResult;
import com.phuclq.student.dto.RentalHouseResultPage;
import com.phuclq.student.repository.RentalHouseRepository;
import com.phuclq.student.service.RentalHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RentalHouseServiceImpl implements RentalHouseService {

    @Autowired
    private RentalHouseRepository rentalHouseRepository;

    @Override
    public RentalHouseResultPage findAll(Pageable pageable) {
        RentalHouseResultPage result = new RentalHouseResultPage();
        List<RentalHouseDto> rentalHouseDtos = new ArrayList<>();
        Page<RentalHouseResult> allHome = rentalHouseRepository.findAllHome(pageable);
        allHome.forEach(x -> {
            RentalHouseDto rentalHouseDto = new RentalHouseDto();

            rentalHouseDto.setId(x.getId());
            rentalHouseDto.setTitle(x.getTitle());
            rentalHouseDto.setPrice(x.getPrice());
            rentalHouseDto.setNumberBeds(x.getNumberBeds());
            rentalHouseDto.setNumberBathroom(x.getNumberBathroom());
            rentalHouseDto.setDistrictName(x.getDistrictName());
            rentalHouseDto.setAcreage(x.getAcreage());
            rentalHouseDto.setNumberToilet(x.getNumberToilet());
            rentalHouseDtos.add(rentalHouseDto);
        });
        result.setRentalHouseDtos(rentalHouseDtos);
        PaginationModel paginationModel = new PaginationModel(allHome.getPageable().getPageNumber(), allHome.getPageable().getPageSize(), (int) allHome.getTotalElements());
        result.setPaginationModel(paginationModel);
        return result;
    }

    @Override
    public RentalHouse findAllById(int id) {
        return rentalHouseRepository.getOne(id);
    }

    @Override
    public RentalHouse save(RentalHouse RentalHouse) {
        return rentalHouseRepository.save(RentalHouse);
    }

    @Override
    public RentalHouse update(RentalHouse RentalHouse) {
        return rentalHouseRepository.save(RentalHouse);
    }

    @Override
    public void deleteById(int id) {
        rentalHouseRepository.deleteById(id);

    }
}
