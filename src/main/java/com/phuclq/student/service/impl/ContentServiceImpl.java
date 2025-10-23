package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Content;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.content.ContentRequest;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.repository.ContentRepository;
import com.phuclq.student.repository.DistrictRepository;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.ConfirmationTokenService;
import com.phuclq.student.service.ContentService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Override
    public Long creatOrUpdateJob(ContentRequest dto) throws IOException {
        Integer login = userService.getUserLogin().getId();
        Content job = new Content();
        if (Objects.nonNull(dto.getId())) {

            job = contentRepository.findById(dto.getId()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
            try {
                Content allByType = contentRepository.findAllByType(dto.getType());
            }catch (Exception e){
                throw new BusinessHandleException("SS023");

            }
        }else {
            Content allByType = contentRepository.findAllByType(Objects.nonNull(dto.getType())?dto.getType():"");
            if(Objects.nonNull(allByType)){
                contentRepository.delete(allByType);

            }
        }
        BeanUtils.copyProperties(dto, job);
        Content result = contentRepository.save(job);
        if (Objects.nonNull(dto.getFiles())) {

            attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), result.getId().intValue(), login, false);
        }
        return result.getId();
    }

    @Override
    public Page<Content> search(Pageable pageable, String search) {
        return !Objects.requireNonNull(search).isEmpty() ? contentRepository.findAllByTitleContainingIgnoreCaseOrTypeContainingIgnoreCase(search.trim(),search.trim(), pageable) : contentRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        contentRepository.deleteById(id);
    }

    @Override
    public Content findAllById(Long id) {
        return contentRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
    }

    @Override
    public Content findAllByType(String type) {
        if (Objects.nonNull(type) && type.startsWith("TOKENT_")) {
            try {
                User user = confirmationTokenService.confirmUserAccount(type);
                return contentRepository.findAllByType("TOKENT");
            } catch (Exception e) {
                return contentRepository.findAllByType("TOKENT");
            }
        }

        return contentRepository.findAllByType(type);
    }
}
