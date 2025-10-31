package com.phuclq.student.service.impl;

import com.phuclq.student.domain.AssetType;
import com.phuclq.student.repository.AssetTypeRepository;
import com.phuclq.student.service.AssetTypeService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AssetTypeServiceImpl implements AssetTypeService {

    private final AssetTypeRepository assetTypeRepository;

    public AssetTypeServiceImpl(AssetTypeRepository assetTypeRepository) {
        this.assetTypeRepository = assetTypeRepository;
    }

    @Override
    public List<AssetType> findAll() {
        return assetTypeRepository.findAll();
    }

    @Override
    public Optional<AssetType> findById(Integer id) {
        return assetTypeRepository.findById(id);
    }

    @Override
    public AssetType save(AssetType assetType) {
        return assetTypeRepository.save(assetType);
    }

    @Override
    public AssetType update(Integer id, AssetType assetType) {
        AssetType existing = assetTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AssetType not found with id: " + id));
        existing.setName(assetType.getName());
        existing.setDescription(assetType.getDescription());
        return assetTypeRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        assetTypeRepository.deleteById(id);
    }
}
