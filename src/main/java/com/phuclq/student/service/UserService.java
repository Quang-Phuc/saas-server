package com.phuclq.student.service;

import com.phuclq.student.domain.File;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    RegisterResponse registryUser(RegisterRequest accountDTO);

    User save(UserSaveDTO accountDTO) throws IOException;

    User saveAdmin(UserSaveDTO accountDTO) throws IOException;

    User findUserByEmail(String email);

    Page<UserDTO> getUser(Pageable pageable);

    UserResultDto getUser2(FileHomePageRequest request, Pageable pageable);

    Optional<User> findUserById(Integer Id);

    User deleteUser(Integer Id);

    User getUserLogin();

    Page<File> getListFileByUser(Integer userId, Pageable pageable);

    Page<User> findUserByUserNameAndEmailAndPhone(UsersSearchRequest usersSearchRequest, Pageable pageable);

    void forgotPassword(String email);

    boolean changePassword(String password, String passwordNew, String passwordConfirm);

    User createAdmin(UserAccountDTO accountDTO);

    void updateAdmin(UserAccountDTO accountDTO);

    UserDTO getUserResultLogin();

    List<UserInfoDTO2> getUserInfos();

    List<UserDTO> getUsersByRole(Integer roleId);

    UserDTO changeRole(AdminRoleDTO adminRoleDTO);

    List<UserInfoResult> findTop10OrderByIdDesc();

    public JwtResponse login(JwtRequest authenticationRequest) throws Exception;

}
