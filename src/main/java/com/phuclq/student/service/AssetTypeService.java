package com.phuclq.student.service;

import com.phuclq.student.domain.AssetType;

import java.util.List;
import java.util.Optional;

public interface AssetTypeService {

    List<AssetType> findAll();

    Optional<AssetType> findById(Integer id);

    AssetType save(AssetType assetType);

    AssetType update(Integer id, AssetType assetType);

    void deleteById(Integer id);
}
