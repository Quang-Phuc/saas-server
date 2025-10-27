package com.phuclq.student.service;

import com.phuclq.student.domain.UserRole;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.UserRepository;
import com.phuclq.student.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        // Tìm user không phân biệt hoa/thường
        Optional<com.phuclq.student.domain.User> userOpt =
                userRepository.findByUserNameIgnoreCaseAndIsDeletedFalse(email);

        if (!userOpt.isPresent()) {
            throw new BusinessHandleException("SS004"); // User không tồn tại
        }

        com.phuclq.student.domain.User user = userOpt.get();

        if (user.getIsEnable() == null || !user.getIsEnable()) {
            throw new BusinessHandleException("SS003"); // Tài khoản bị khóa
        }

        // Lấy role
        Optional<UserRole> userRoleOptional = userRoleRepository.findById(user.getRoleId());
        String roleName = userRoleOptional.map(UserRole::getRole).orElse("USER");

        // Trả về đối tượng UserDetails cho Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail() != null ? user.getEmail() : user.getUserName(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName))
        );
    }

}

