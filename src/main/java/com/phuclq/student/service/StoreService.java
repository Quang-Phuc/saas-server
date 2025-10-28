package com.phuclq.student.service;

import com.phuclq.student.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StoreService {

    Store create(Store store);

    Store update(Long id, Store store);

    void delete(Long id);

    Page<Map<String, Object>> getAll(String keyword, String type, Pageable pageable);
}
