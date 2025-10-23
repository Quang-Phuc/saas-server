package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class RentalHouseResultPage {
    List<RentalHouseDto> rentalHouseDtos;
    PaginationModel paginationModel;


}
