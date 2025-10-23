package com.phuclq.student.dto;

import com.phuclq.student.domain.Auditable;
import lombok.Data;

@Data
public class RentalHouseDto extends Auditable<String> {

    private Long id;

    private Integer idProvince;

    private Integer idDistrict;

    private Integer idWard;

    private Integer idStreet;

    private String address;

    private String title;

    private String districtName;

    private Integer numberBeds;

    private Double price;

    private Integer numberBathroom;

    private Boolean closed;

    private Boolean sharedRoom;

    private Integer userId;

    private Integer acreage;

    private Integer numberToilet;
}
