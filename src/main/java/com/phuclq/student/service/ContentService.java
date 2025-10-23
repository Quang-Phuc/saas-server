package com.phuclq.student.service;

import com.phuclq.student.domain.Content;
import com.phuclq.student.dto.content.ContentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ContentService {


    Long creatOrUpdateJob(ContentRequest contentRequest) throws IOException;


    Page<Content> search(Pageable pageable, String search);

    Content findAllById(Long id);

    Content findAllByType(String type);

    void deleteById(Long id);

}
