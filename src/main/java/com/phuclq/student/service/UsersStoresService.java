package com.phuclq.student.service;

import com.phuclq.student.domain.UsersStores;
import java.util.List;

public interface UsersStoresService {

    List<UsersStores> findAll();

    UsersStores findById(Long id);

    UsersStores create(UsersStores usersStores);

    UsersStores update(Integer id, UsersStores usersStores);

    void delete(Long id);
}
