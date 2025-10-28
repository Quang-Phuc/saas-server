package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Store;
import com.phuclq.student.domain.UsersStores;
import com.phuclq.student.dto.StoreDTO;
import com.phuclq.student.dto.StoreDropdownDTO;
import com.phuclq.student.dto.StoreWithUserDto;
import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.repository.StoreRepository;
import com.phuclq.student.repository.UsersStoresRepository;
import com.phuclq.student.service.StoreService;
import com.phuclq.student.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UsersStoresRepository usersStoresRepository;
    private final UserService userService;

    @Override
    public Store create(StoreDTO store) {
        UserDTO user = userService.getUserResultLogin();
        Store store1 = new Store();
        store1.setName(store.getStoreName());
        store1.setAddress(store.getStoreAddress());
        store1.setNote(store.getNotes());
        Store savedStore = storeRepository.save(store1);

        // Ghi vào bảng USERS_STORES
        UsersStores link = new UsersStores();
        link.setUserId(user.getId());
        link.setStoreId(savedStore.getId());
        usersStoresRepository.save(link);

        return savedStore;
    }

    @Override
    public Store update(Long id, StoreDTO store) {
        Store existing = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng"));
        existing.setName(store.getStoreName());
        existing.setAddress(store.getStoreAddress());
        existing.setNote(store.getNotes());
        return storeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        // Xóa liên kết trước
        usersStoresRepository.deleteByStoreId(id);
        storeRepository.deleteById(id);
    }

    @Override
    public Page<StoreWithUserDto> getAll(String keyword, String type, Pageable pageable) {
        UserDTO user = userService.getUserResultLogin();
        Integer userId = "user".equalsIgnoreCase(type) ? user.getId() : null;

        Page<Map<String, Object>> rawPage = storeRepository.searchStoresWithUser(keyword, userId, pageable);

        return rawPage.map(map -> StoreWithUserDto.builder()
                .storeId(((Number) map.get("store_id")).longValue())
                .notes((String) map.get("note"))
                .storeName((String) map.get("store_name"))
                .storeAddress((String) map.get("store_address"))
                .userId(((Number) map.get("user_id")).intValue())
                .userFullName((String) map.get("user_full_name"))
                .userPhone((String) map.get("user_phone"))
                .userEmail((String) map.get("user_email"))
                .userRoleId(map.get("user_role_id") != null ? ((Number) map.get("user_role_id")).intValue() : null)
                .createdDate(map.get("created_date") != null ?
                        ((java.sql.Timestamp) map.get("created_date")).toLocalDateTime() : null)
                .build());
    }

    @Override
    public List<StoreDropdownDTO> getStoresDropdown(Integer userId) {
        UserDTO user = userService.getUserResultLogin();
        Integer finalUserId = (userId != null) ? userId : user.getId();

        List<Map<String, Object>> list = storeRepository.findStoresByUserId(finalUserId);

        return list.stream().map(map -> StoreDropdownDTO.builder()
                .id(map.get("id") != null ? ((Number) map.get("id")).longValue() : null)
                .name((String) map.get("name"))
                .address((String) map.get("address"))
                .build()
        ).collect(Collectors.toList());
    }


}
