package com.phuclq.student.service;

import com.phuclq.student.domain.UserInformation;
import com.phuclq.student.dto.UserInformationDTO;
import com.phuclq.student.dto.UserInformationDetailDTO;

public interface UserInformationService {
    UserInformation save(UserInformationDTO userInformationDTO);

    UserInformation update(UserInformationDTO userInformationDTO);

    void delete(Integer Id);

    UserInformationDetailDTO listById();
}
