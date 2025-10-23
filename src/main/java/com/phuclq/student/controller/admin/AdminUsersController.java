package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.*;
import com.phuclq.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminUsersController {
    @Autowired
    private UserService userService;

    @Autowired
    private RestEntityResponse restEntityRes;


    @PostMapping("/admin/add-admin")
    public ResponseEntity<?> createAdmin(@RequestBody @Valid UserAccountDTO accountDTO) throws Exception {
        User existingUser = userService.findUserByEmail(accountDTO.getEmail());
        if (existingUser != null) {
            throw new Exception("This email already exists!");
        }
        User createAdm = userService.createAdmin(accountDTO);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(createAdm).getResponse();

    }

    @PutMapping("/admin/update-admin")
    public ResponseEntity<?> updateAdmin(@RequestBody @Valid UserAccountDTO accountDTO) throws Exception {
        userService.updateAdmin(accountDTO);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();

    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @GetMapping("/admin/get-user/{roleId}")
    public ResponseEntity<?> updateAdmin(@PathVariable("roleId") Integer roleId) {
        List<UserDTO> data = userService.getUsersByRole(roleId);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(data).getResponse();

    }


    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @DeleteMapping("/user/delete/{Id}")
    public ResponseEntity<?> deleteUser(@PathVariable("Id") Integer id) {
        User user = userService.deleteUser(id);
        String message = "Xóa " + user.getUserName() + " thành công";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(message).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/change-role")
    public ResponseEntity<?> changeRole(@RequestBody AdminRoleDTO adminRoleDTO) {
        if (adminRoleDTO == null) {
            String message = "Có lỗi xảy ra vui lòng thử lại";
            return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(message).getResponse();
        }
        UserDTO user = userService.changeRole(adminRoleDTO);
        if (user == null) {
            String message = "Có lỗi xảy ra vui lòng thử lại";
            return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(message).getResponse();
        } else {
            String message = "Cập nhật thành công";
            return restEntityRes.setHttpStatus(HttpStatus.CREATED).setDataResponse(message).getResponse();
        }
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestBody FileHomePageRequest request, Pageable pageable) {
        UserResultDto list = userService.getUser2(request, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(list).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserSaveDTO accountDTO) throws IOException {

        User user = userService.saveAdmin(accountDTO);
        return restEntityRes.setHttpStatus(HttpStatus.CREATED).setDataResponse(user.getUserName()).getResponse();
    }

}
