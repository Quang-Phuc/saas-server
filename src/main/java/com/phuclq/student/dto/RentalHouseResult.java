package com.phuclq.student.dto;


public interface RentalHouseResult {

    Long getId();

    Integer getIdProvince();

    Integer getDistrict();

    Integer getDistrictStr();

    Integer getIdWard();

    Integer getIdStreet();

    String getAddress();

    Double getPrice();

    String getTitle();

    Integer getNumberBeds();

    String getDistrictName();

    Integer getNumberBathroom();

    Boolean getClosed();

    Boolean getSharedRoom();

    Integer userId();

    Integer getAcreage();

    Integer getNumberToilet();

}
