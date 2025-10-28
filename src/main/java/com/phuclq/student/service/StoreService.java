package com.phuclq.student.service;

import com.phuclq.student.domain.Store;
import com.phuclq.student.dto.StoreDTO;
import com.phuclq.student.dto.StoreDropdownDTO;
import com.phuclq.student.dto.StoreWithUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface StoreService {

    Store create(StoreDTO store);

    Store update(Long id, StoreDTO store);

    void delete(Long id);

    Page<StoreWithUserDto> getAll(String keyword, String type, Pageable pageable);

    List<StoreDropdownDTO> getStoresDropdown(Integer userId);
}
