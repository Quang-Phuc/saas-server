package com.phuclq.student.service;

import com.phuclq.student.domain.UsersStores;
import com.phuclq.student.dto.UserStoreInfoDTO;

import java.util.List;

public interface UsersStoresService {

    List<UsersStores> findAll();

    UsersStores findById(Long id);

    UsersStores create(UsersStores usersStores);

    UsersStores update(Integer id, UsersStores usersStores);

    void delete(Long id);

    List<UserStoreInfoDTO> getUsersByStoreId(Long storeId);
}
