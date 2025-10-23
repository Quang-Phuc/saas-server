package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Attachment;
import com.phuclq.student.domain.Banner;
import com.phuclq.student.dto.banner.BannerRequest;
import com.phuclq.student.dto.banner.BannerResultDto;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.exception.NotFoundException;
import com.phuclq.student.repository.BannerRepository;
import com.phuclq.student.repository.DistrictRepository;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.BannerService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.phuclq.student.types.FileType.FILE_BANNER;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public Long creatOrUpdate(BannerRequest dto) throws IOException {
        Integer login = userService.getUserLogin().getId();
        Banner banner = new Banner();


        if (Objects.nonNull(dto.getId())) {

            banner = bannerRepository.findById(dto.getId()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
           try {
               Banner allByType = bannerRepository.findAllByType(dto.getType());
           }catch (Exception e){
               throw new BusinessHandleException("SS023");

           }

        }else {
            Banner allByType = bannerRepository.findAllByType(Objects.nonNull(dto.getType())?dto.getType():"");
            if(Objects.nonNull(allByType)){
                bannerRepository.delete(allByType);
            }
        }
        BeanUtils.copyProperties(dto, banner);
        Banner result = bannerRepository.save(banner);
        if (Objects.nonNull(dto.getFiles())) {
            dto.getFiles().forEach(x->{
                x.setType(dto.getType());
            });
            attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), result.getId().intValue(), login, false);
        }
        return result.getId();
    }

    @Override
    public Page<Banner> search(Pageable pageable, String search) {
        return !Objects.requireNonNull(search).isEmpty() ? bannerRepository.findAllByTypeContainingIgnoreCase(search.trim(), pageable) : bannerRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        Banner banner = bannerRepository.findById(id).orElseThrow(NotFoundException::new);
        attachmentService.deleteAttachmentByRequestId(banner.getId().intValue(),banner.getType());
        bannerRepository.delete(banner);
    }

    @Override
    public BannerResultDto findAllById(Long id) {
        BannerResultDto bannerResultDto = new BannerResultDto();
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
        List<Attachment> attachmentByRequestIdAndType = attachmentService.getAttachmentByRequestIdAndType(id.intValue(), banner.getType());
        bannerResultDto.setBanner(banner);
        bannerResultDto.setAttachments(attachmentByRequestIdAndType);
        return bannerResultDto;
    }

    @Override
    public BannerResultDto findAllByType(String type) {
        BannerResultDto bannerResultDto = new BannerResultDto();
        Banner banner = bannerRepository.findAllByType(type);
        if (Objects.nonNull(banner)) {
            List<Attachment> attachmentByRequestIdAndType = attachmentService.getAttachmentByRequestIdAndType(banner.getId().intValue(), type.toUpperCase().trim());
            bannerResultDto.setBanner(banner);
            bannerResultDto.setUrls(attachmentByRequestIdAndType.stream().map(Attachment::getUrl).collect(Collectors.toList()));
        }
        return bannerResultDto;
    }
}
