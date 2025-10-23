package com.phuclq.student.repository;

import com.phuclq.student.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findById(Integer id);

    Optional<UserRole> findByRole(String role);

}
