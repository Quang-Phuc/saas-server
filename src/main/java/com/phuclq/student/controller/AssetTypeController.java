package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.AssetType;
import com.phuclq.student.dto.AssetTypeDTO;
import com.phuclq.student.dto.AssetTypeResponse;
import com.phuclq.student.service.AssetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset-types")
public class AssetTypeController {

    private final AssetTypeService assetTypeService;

    @Autowired
    private RestEntityResponse restEntityRes;

    public AssetTypeController(AssetTypeService assetTypeService) {
        this.assetTypeService = assetTypeService;
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<List<AssetTypeResponse>> getAll(@PathVariable Long id) {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(assetTypeService.findAll(id)).getResponse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetType> getById(@PathVariable Integer id) {
        return assetTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AssetType> create(@RequestBody AssetTypeDTO assetType) {
        AssetType saved = assetTypeService.save(assetType);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetType> update(@PathVariable Integer id, @RequestBody AssetType assetType) {
        return ResponseEntity.ok(assetTypeService.update(id, assetType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        assetTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
