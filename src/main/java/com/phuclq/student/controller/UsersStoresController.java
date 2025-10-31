package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.UsersStores;
import com.phuclq.student.dto.UserStoreInfoDTO;
import com.phuclq.student.service.UsersStoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users-stores")
public class UsersStoresController {

    @Autowired
    private UsersStoresService service;

    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping
    public List<UsersStores> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UsersStores getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public UsersStores create(@RequestBody UsersStores usersStores) {
        return service.create(usersStores);
    }

    @PutMapping("/{id}")
    public UsersStores update(@PathVariable Integer id, @RequestBody UsersStores usersStores) {
        return service.update(id, usersStores);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getUsersByStoreId(@PathVariable Long storeId) {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(service.getUsersByStoreId(storeId)).getResponse();
    }
}
