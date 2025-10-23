package com.phuclq.student.dto.school;


import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface SchoolResult {

    Long getId();

    Integer getIdProvince();

    Integer getDistrict();

    Integer getDistrictStr();

    Integer getIdWard();

    Integer getIdStreet();

    String getAddress();

    Double getPrice();

    String getTitle();

    String getWardName();

    String getProvinceName();

    Integer getNumberBeds();

    String getDistrictName();

    Integer getNumberBathroom();

    Boolean getClosed();

    Boolean getSharedRoom();

    Integer userId();

    Integer getAcreage();

    Integer getNumberToilet();


    String getSchoolName();

    String getContent();

    String getType();

    Integer getWardId();

    String getIdUrl();

    Integer getDistrictId();

    Integer getProvinceId();


    String getSummary();

    Integer getTotalStudent();

    Integer getTotalComment();

    Integer getApproverId();

    Timestamp getApprovedDate();

    LocalDateTime getCreatedDate();

     String getSchoolType();

     String getSchoolTypeEducation();
}
