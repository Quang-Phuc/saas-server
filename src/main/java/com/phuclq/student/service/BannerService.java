package com.phuclq.student.service;

import com.phuclq.student.domain.Banner;
import com.phuclq.student.dto.banner.BannerRequest;
import com.phuclq.student.dto.banner.BannerResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface BannerService {

    Long creatOrUpdate(BannerRequest bannerRequest) throws IOException;


    Page<Banner> search(Pageable pageable, String search);

    BannerResultDto findAllById(Long id);

    BannerResultDto findAllByType(String id);

    void deleteById(Long id);


}
