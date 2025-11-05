package com.phuclq.student.service;

import com.phuclq.student.domain.AssetType;
import com.phuclq.student.dto.AssetTypeDTO;
import com.phuclq.student.dto.AssetTypeResponse;

import java.util.List;
import java.util.Optional;

public interface AssetTypeService {

    List<AssetTypeResponse> findAll(Long storeId);

    Optional<AssetType> findById(Integer id);

    AssetType save(AssetTypeDTO assetType);

    AssetType update(Integer id, AssetType assetType);

    void deleteById(Integer id);
}
