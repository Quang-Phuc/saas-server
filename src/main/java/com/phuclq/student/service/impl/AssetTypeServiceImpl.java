package com.phuclq.student.service.impl;

import com.phuclq.student.domain.AssetType;
import com.phuclq.student.domain.AssetTypeAttribute;
import com.phuclq.student.dto.AssetTypeDTO;
import com.phuclq.student.dto.AssetTypeResponse;
import com.phuclq.student.repository.AssetTypeAttributeRepository;
import com.phuclq.student.repository.AssetTypeRepository;
import com.phuclq.student.service.AssetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetTypeServiceImpl implements AssetTypeService {

    private final AssetTypeRepository assetTypeRepository;
    private final AssetTypeAttributeRepository assetTypeAttributeRepository;


    @Override
    public List<AssetTypeResponse> findAll(Long storeId) {
        List<AssetType> assetTypes = assetTypeRepository.findAllByStoreIdOrNull(storeId);

        List<Long> ids = assetTypes.stream()
                .map(a -> a.getId().longValue())
                .collect(Collectors.toList());;

        List<AssetTypeAttribute> allAttributes = assetTypeAttributeRepository.findByAssetTypeIds(ids);

        return assetTypes.stream().map(assetType -> {
            AssetTypeResponse dto = new AssetTypeResponse();
            dto.setId(assetType.getId());
            dto.setName(assetType.getTypeCode());
            dto.setDescription(assetType.getTypeName());
            dto.setStoreId(assetType.getStoreId());

            List<AssetTypeResponse.AttributeDto> attrList = allAttributes.stream()
                    .filter(attr -> attr.getAssetTypeId().equals(assetType.getId().longValue()))
                    .map(attr -> {
                        AssetTypeResponse.AttributeDto adto = new AssetTypeResponse.AttributeDto();
                        adto.setId(attr.getId());
                        adto.setLabel(attr.getLabel());
                        adto.setAssetTypeId(attr.getAssetTypeId());
                        return adto;
                    })
                    .collect(Collectors.toList());;

            dto.setAttributes(attrList);
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public Optional<AssetType> findById(Integer id) {
        return assetTypeRepository.findById(id);
    }

    @Override
    @Transactional
    public AssetType save(AssetTypeDTO dto) {
        // Lưu assetType
        AssetType assetType = new AssetType();
        assetType.setTypeCode(dto.getTypeCode());
        assetType.setTypeName(dto.getTypeName());
        assetType.setStoreId(dto.getStoreId());
        assetType.setStatus(dto.getStatus());
        AssetType saved = assetTypeRepository.save(assetType);

        // Nếu có attributes trong request
        if (dto.getAttributes() != null && !dto.getAttributes().isEmpty()) {
            Long typeId = saved.getId().longValue();

            // Xóa attribute cũ (nếu update)
            assetTypeAttributeRepository.deleteByAssetTypeId(typeId);

            // Thêm mới
            for (AssetTypeAttribute attr : dto.getAttributes()) {
                attr.setAssetTypeId(typeId);
                assetTypeAttributeRepository.save(attr);
            }
        }

        return saved;
    }

    @Override
    public AssetType update(Integer id, AssetType assetType) {
        AssetType existing = assetTypeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("AssetType not found with id: " + id));
        existing.setTypeCode(assetType.getTypeCode());
        existing.setTypeName(assetType.getTypeName());
        return assetTypeRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        assetTypeRepository.deleteById(id);
    }
}
