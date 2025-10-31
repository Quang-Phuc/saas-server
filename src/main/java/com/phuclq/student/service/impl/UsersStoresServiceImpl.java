package com.phuclq.student.service.impl;

import com.phuclq.student.domain.User;
import com.phuclq.student.domain.UsersStores;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.UserRepository;
import com.phuclq.student.repository.UsersStoresRepository;
import com.phuclq.student.service.UsersStoresService;
import com.phuclq.student.types.RoleConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phuclq.student.dto.UserStoreInfoDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UsersStoresServiceImpl implements UsersStoresService {

    @Autowired
    private UsersStoresRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UsersStores> findAll() {
        return repository.findAll();
    }

    @Override
    public UsersStores findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UsersStores not found with id: " + id));
    }

    @Override
    public UsersStores create(UsersStores usersStores) {
        return repository.save(usersStores);
    }

    @Override
    public UsersStores update(Integer id, UsersStores usersStores) {
        if (usersStores.getType() == null) {
            // 🔹 Nếu type = null → update bản ghi theo id
            usersStores.setId(Long.valueOf(id));
            return repository.save(usersStores);
        } else {
            // 🔹 Nếu type != null → tìm theo userId & storeId rồi xóa
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("UsersStores not found with id: " + id));
            if (user.getRoleId().equals(RoleConstant.OWNER)){
                throw new BusinessHandleException("SS004");
            }
            UsersStores existing = repository
                    .findByUserIdAndStoreId(usersStores.getUserId(), usersStores.getStoreId())
                    .orElseThrow(() -> new RuntimeException("UsersStores not found for delete"));
            repository.delete(existing);
            return null; // hoặc return existing nếu bạn muốn trả bản ghi đã xóa
        }
    }


    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
    @Override
    public List<UserStoreInfoDTO> getUsersByStoreId(Long storeId) {
        List<User> users = repository.findUsersByStoreId(storeId);
        return users.stream()
                .map(u -> new UserStoreInfoDTO(u.getId(), u.getFullName(), u.getPhone()))
                .collect(Collectors.toList());
    }
}
