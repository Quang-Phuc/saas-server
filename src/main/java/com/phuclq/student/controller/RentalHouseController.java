package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.RentalHouse;
import com.phuclq.student.dto.RentalHouseResultPage;
import com.phuclq.student.service.RentalHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RentalHouseController {
    @Autowired
    private RentalHouseService RentalHouseService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("/rentalhouse")
    public ResponseEntity<?> getAllRentalHouse(Pageable pageable) {

        RentalHouseResultPage result = RentalHouseService.findAll(pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/rentalhouse/create")
    public ResponseEntity<?> createRentalHouse(@RequestBody RentalHouse RentalHouse) {
        RentalHouse RentalHouseResult = RentalHouseService.save(RentalHouse);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(RentalHouseResult).getResponse();
    }

    @PutMapping("/rentalhouse/update")
    public ResponseEntity<?> updateRentalHouse(@RequestBody RentalHouse RentalHouse) {

        RentalHouse RentalHouseResult = RentalHouseService.update(RentalHouse);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(RentalHouseResult).getResponse();
    }

    @DeleteMapping("/rentalhouse/delete/{Id}")
    public ResponseEntity<?> deleteRentalHouse(@PathVariable int Id) {

        RentalHouseService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/rentalhouse/id")
    public ResponseEntity<?> findByIdRentalHouse(@PathVariable int Id) {
        RentalHouse RentalHouse = RentalHouseService.findAllById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(RentalHouse).getResponse();
    }
}
