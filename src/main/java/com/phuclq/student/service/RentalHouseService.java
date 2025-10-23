package com.phuclq.student.service;

import com.phuclq.student.domain.RentalHouse;
import com.phuclq.student.dto.RentalHouseResultPage;
import org.springframework.data.domain.Pageable;

public interface RentalHouseService {
    RentalHouseResultPage findAll(Pageable pageable);

    RentalHouse findAllById(int id);

    RentalHouse save(RentalHouse RentalHouse);

    RentalHouse update(RentalHouse RentalHouse);

    void deleteById(int id);
}
