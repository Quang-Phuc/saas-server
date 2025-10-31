package com.phuclq.student.service;

import com.phuclq.student.domain.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    List<Warehouse> findAll();
    Optional<Warehouse> findById(Long id);
    Warehouse save(Warehouse warehouse);
    void delete(Long id);
}
