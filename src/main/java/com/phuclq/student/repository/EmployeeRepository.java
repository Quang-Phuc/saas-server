package com.phuclq.student.repository;

import com.phuclq.student.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByStoreId(Long storeId);

    long countByStoreId(Long storeId);

    List<Employee> findByUserId(Long userId);
}
