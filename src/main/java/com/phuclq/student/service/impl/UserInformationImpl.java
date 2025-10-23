package com.phuclq.student.service.impl;

import com.phuclq.student.domain.User;
import com.phuclq.student.domain.UserInformation;
import com.phuclq.student.dto.UserInformationDTO;
import com.phuclq.student.dto.UserInformationDetailDTO;
import com.phuclq.student.repository.UserInformationRepository;
import com.phuclq.student.repository.UserRepository;
import com.phuclq.student.service.UserInformationService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class UserInformationImpl implements UserInformationService {
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public UserInformation save(UserInformationDTO userInformationDTO) {
        User userLogin = userService.getUserLogin();
        userLogin.setUserName(userInformationDTO.getUserName());
        userLogin.setPhone(userInformationDTO.getPhone());
        userRepository.save(userLogin);
        UserInformation userInformation = new UserInformation();
        userInformation.setGender(userInformationDTO.getGender());
        userInformation.setBirthDate(DateTimeUtils.toTimestampFromStr(userInformationDTO.getBirthDate(), DateTimeUtils.yyyy_MM_dd));
        userInformation.setAddress(userInformationDTO.getAddress());
        userInformation.setUserId(userLogin.getId());
        userInformation.setSpecialized(userInformationDTO.getSpecialize());
        userInformation.setYourself(userInformationDTO.getYourself());
        userInformation.setSchoolId(userInformationDTO.getSchoolId());
        return userInformationRepository.save(userInformation);
    }

    @Override
    public UserInformation update(UserInformationDTO userInformationDTO) {
        User userLogin = userService.getUserLogin();
        userLogin.setUserName(userInformationDTO.getUserName());
        userLogin.setPhone(userInformationDTO.getPhone());
        userRepository.save(userLogin);

        UserInformation userInformationId = userInformationRepository.findAllByUserId(userLogin.getId());
        if (userInformationId == null) {
            userInformationId = new UserInformation();
        }
        userInformationId.setId(userLogin.getId());
        userInformationId.setGender(userInformationDTO.getGender());
        userInformationId.setBirthDate(DateTimeUtils.toTimestampFromStr(userInformationDTO.getBirthDate(), DateTimeUtils.yyyy_MM_dd));
        userInformationId.setAddress(userInformationDTO.getAddress());
        userInformationId.setUserId(userLogin.getId());
        userInformationId.setSpecialized(userInformationDTO.getSpecialize());
        userInformationId.setYourself(userInformationDTO.getYourself());
        userInformationId.setSchoolId(userInformationDTO.getSchoolId());
        return userInformationRepository.save(userInformationId);
    }

    @Override
    public void delete(Integer Id) {
        userInformationRepository.deleteById(Id);
    }

    @Override
    public UserInformationDetailDTO listById() {
        User userLogin = userService.getUserLogin();
        return userInformationRepository.findAllById(userLogin.getId()).stream().map(UserInformationDetailDTO::new).collect(Collectors.toList()).get(0);

    }
}
