package com.phuclq.student.repository;

import com.phuclq.student.domain.Store;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
List<Store> findAllByOwnerId(Integer ownerId);
}
