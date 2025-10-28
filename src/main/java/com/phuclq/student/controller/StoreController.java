package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Store;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.UserPaymentInfor;
import com.phuclq.student.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping
    public Store create(@RequestBody Store store) {
        return storeService.create(store);
    }

    @PutMapping("/{id}")
    public Store update(@PathVariable Long id, @RequestBody Store store) {
        return storeService.update(id, store);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        storeService.delete(id);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "admin") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("created_date").descending());
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(storeService.getAll(keyword, type, pageable)).getResponse();
    }


}
