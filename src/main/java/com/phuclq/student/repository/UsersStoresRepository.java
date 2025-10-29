package com.phuclq.student.repository;

import com.phuclq.student.domain.Store;
import com.phuclq.student.domain.UsersStores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersStoresRepository extends JpaRepository<UsersStores, Long> {
    List<UsersStores> findAllByUserId(Integer userId);
    List<UsersStores> findByStoreId(Long storeId);
    void deleteByStoreId(Long storeId);

    Optional<UsersStores> findByUserIdAndStoreId(Integer userId, Long storeId);

}
