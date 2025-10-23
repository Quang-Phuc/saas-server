package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Attachment;
import com.phuclq.student.domain.Sale;
import com.phuclq.student.dto.sale.SaleRequest;
import com.phuclq.student.dto.sale.SaleResultDto;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.repository.DistrictRepository;
import com.phuclq.student.repository.SaleRepository;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.SaleService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.FileType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private SaleRepository saleRepository;

    @Override
    public Long creatOrUpdate(SaleRequest dto) throws IOException {
        Integer login = userService.getUserLogin().getId();
        Sale sale = new Sale();
        if (Objects.nonNull(dto.getId())) {

            sale = saleRepository.findById(dto.getId()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
            if (dto.getStatus()) {
                Sale allByStatus = saleRepository.findAllByStatus(true);
                if (Objects.nonNull(allByStatus)&& !sale.getId().equals(allByStatus.getId())) {
                    throw new BusinessHandleException("SS019");
                }
            }
        }
        BeanUtils.copyProperties(dto, sale);
        Sale result = saleRepository.save(sale);
        if(Objects.nonNull(dto.getFiles())&& Objects.nonNull(dto.getFiles().stream().findFirst().get().getName())) {

            List<Attachment> listAttachmentsFromBase64S3 = attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), result.getId().intValue(), login, true);
            result.setUrl(listAttachmentsFromBase64S3.get(0).getUrl());
            saleRepository.save(result);
        }
        return result.getId();
    }

    @Override
    public Page<Sale> search(Pageable pageable, String search) {
        return !Objects.requireNonNull(search).isEmpty() ? saleRepository.findAllByTitleContainingIgnoreCase(search.trim(), pageable) : saleRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        saleRepository.deleteById(id);
        attachmentService.deleteAttachmentByRequestId(id.intValue(), FileType.SALE_IMAGE.getName());
    }

    @Override
    public SaleResultDto findAllById(Long id) {

        SaleResultDto saleResultDto = new SaleResultDto();
        saleResultDto.setSale(saleRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST)));
        List<Attachment> attachmentByRequestIdAndType = attachmentService.getAttachmentByRequestIdAndType(id.intValue(), FileType.SALE_IMAGE.getName());
        saleResultDto.setAttachments(attachmentByRequestIdAndType);
        saleResultDto.setUrl(attachmentByRequestIdAndType.get(0).getUrl());
        return saleResultDto;
    }

    @Override
    public SaleResultDto findAllStatus() {

        SaleResultDto saleResultDto = new SaleResultDto();
        // Lấy ngày hôm nay
        LocalDateTime now = LocalDateTime.now();

        // Lấy ngày hôm nay và đặt thời gian cuối cùng (23:59:59.999999999)
        LocalDateTime endOfDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
        Sale allByStatus = saleRepository.findAllByEndDateBeforeOrEqual(endOfDay);
        if(Objects.nonNull(allByStatus)) {
            List<Attachment> attachmentByRequestIdAndType = attachmentService.getAttachmentByRequestIdAndType(allByStatus.getId().intValue(), FileType.SALE_IMAGE.getName());
            saleResultDto.setUrl(attachmentByRequestIdAndType.get(0).getUrl());
            saleResultDto.setContent("SALE_CONTENT");
        }else {
            return null;
        }
        return saleResultDto;
    }
}
