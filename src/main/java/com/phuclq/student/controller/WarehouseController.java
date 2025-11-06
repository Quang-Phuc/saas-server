package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Warehouse;
import com.phuclq.student.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("/store/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id) {
        return restEntityRes.setHttpStatus(HttpStatus.CREATED).setDataResponse(warehouseService.findAll()).getResponse();
    }

    @GetMapping("/{id}")
    public Warehouse getById(@PathVariable Long id) {
        return warehouseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id " + id));
    }

    @PostMapping("/store/{id}")
    public Warehouse create(@RequestBody Warehouse warehouse,@PathVariable Long id) {
        warehouse.setStoreId(id);
        return warehouseService.save(warehouse);
    }

    @PutMapping("/{id}")
    public Warehouse update(@PathVariable Long id, @RequestBody Warehouse warehouse) {
        warehouse.setId(id);
        return warehouseService.save(warehouse);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        warehouseService.delete(id);
    }
}
