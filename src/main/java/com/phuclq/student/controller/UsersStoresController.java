package com.phuclq.student.controller;

import com.phuclq.student.domain.UsersStores;
import com.phuclq.student.service.UsersStoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users-stores")
public class UsersStoresController {

    @Autowired
    private UsersStoresService service;

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
}
