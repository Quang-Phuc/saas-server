package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Banner;
import com.phuclq.student.dto.banner.BannerRequest;
import com.phuclq.student.dto.banner.BannerResultDto;
import com.phuclq.student.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/banner")
public class BannerController {
    @Autowired
    private RestEntityResponse restEntityRes;

    @Autowired
    private BannerService contentService;

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("")
    public ResponseEntity<?> creatOrUpdateJob(@RequestBody BannerRequest bannerRequest) throws IOException {
        Long jobServiceCV = contentService.creatOrUpdate(bannerRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("/search")
    public ResponseEntity<?> search(Pageable pageable, String search) throws IOException {
        Page<Banner> jobServiceCV = contentService.search(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        contentService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        BannerResultDto resultDto = contentService.findAllById(id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(resultDto).getResponse();
    }

    @GetMapping("/detail/type/{type}")
    public ResponseEntity<?> findByType(@PathVariable String type) {
        BannerResultDto resultDto = contentService.findAllByType(type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(resultDto).getResponse();
    }


}
