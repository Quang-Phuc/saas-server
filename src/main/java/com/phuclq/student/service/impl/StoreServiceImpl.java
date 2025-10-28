package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Store;
import com.phuclq.student.domain.UsersStores;
import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.repository.StoreRepository;
import com.phuclq.student.repository.UsersStoresRepository;
import com.phuclq.student.service.StoreService;
import com.phuclq.student.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UsersStoresRepository usersStoresRepository;
    private final UserService userService;

    @Override
    public Store create(Store store) {
        UserDTO user = userService.getUserResultLogin();
        Store savedStore = storeRepository.save(store);

        // Ghi vào bảng USERS_STORES
        UsersStores link = new UsersStores();
        link.setUserId(user.getId());
        link.setStoreId(savedStore.getId());
        usersStoresRepository.save(link);

        return savedStore;
    }

    @Override
    public Store update(Long id, Store store) {
        Store existing = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng"));
        existing.setName(store.getName());
        existing.setAddress(store.getAddress());
        return storeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        // Xóa liên kết trước
        usersStoresRepository.deleteByStoreId(id);
        storeRepository.deleteById(id);
    }

    @Override
    public Page<Map<String, Object>> getAll(String keyword, String type, Pageable pageable) {
        UserDTO user = userService.getUserResultLogin();
        Integer userId = "user".equalsIgnoreCase(type) ? user.getId() : null;
        return storeRepository.searchStoresWithUser(keyword, userId, pageable);
    }
}
