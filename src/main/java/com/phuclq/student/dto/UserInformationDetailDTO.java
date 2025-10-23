package com.phuclq.student.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInformationDetailDTO {
    String userName;
    Short gender;
    Integer address;
    String phone;
    Integer specialize;
    String yourself;
    LocalDate birthDate;
    Integer schoolId;

    public UserInformationDetailDTO(UserInformationResult userInformationResult) {
        this.userName = userInformationResult.getUserName();
        if (userInformationResult.getBirthDate() != null)
            this.birthDate = userInformationResult.getBirthDate().toLocalDateTime().toLocalDate();
        this.gender = userInformationResult.getGender();
        this.address = userInformationResult.getAddress();
        this.phone = userInformationResult.getPhone();
        this.specialize = userInformationResult.getSpecialized();
        this.yourself = userInformationResult.getYourself();
        this.schoolId = userInformationResult.getSchoolId();
    }
}

