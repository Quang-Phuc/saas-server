package com.phuclq.student.service.impl;

import com.phuclq.student.domain.*;
import com.phuclq.student.dto.AttachmentDTO;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.SchoolResultDto;
import com.phuclq.student.dto.school.SchoolRequest;
import com.phuclq.student.dto.school.SchoolResult;
import com.phuclq.student.dto.school.SchoolResultDetail;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.exception.NotFoundException;
import com.phuclq.student.repository.*;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.SchoolService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.RateType;
import com.phuclq.student.utils.CommonFunction;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.phuclq.student.types.FileType.SCHOOL_IMAGE;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;
import static java.util.stream.Collectors.averagingDouble;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    RateRepository rateRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SchoolTypeRepository schoolTypeRepository;

    @Override
    public List<School> findAll() {
        return schoolRepository.findAll();
    }

    @Override
    public SchoolResultDetail findAllById(String idUrl) throws IOException {
        User userLogin = userService.getUserLogin();
        Integer loginId = userLogin.getId();
        School schoolById = schoolRepository.findByIdUrl(idUrl).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
        List<AttachmentDTO> attachmentByRequestIdFromS3S = attachmentService.getAttachmentByRequestIdFromS3S(schoolById.getId().intValue(), SCHOOL_IMAGE.getName());
        SchoolResultDetail schoolResultDetail = new SchoolResultDetail();
        List<Attachment> allByRequestId = attachmentRepository.findAllByRequestIdAndFileType(schoolById.getId().intValue(), SCHOOL_IMAGE.getName());
        schoolResultDetail.setUrls(allByRequestId.stream().map(Attachment::getUrl).collect(Collectors.toList()));
        schoolResultDetail.setAttachmentDTOList(attachmentByRequestIdFromS3S);
        schoolResultDetail.setSchool(schoolById);
        List<Rate> allByRequestIdAndType = rateRepository.findAllByRequestIdAndType(schoolById.getIdUrl(), RateType.RATE_SCHOOL.getName());
        if (Objects.nonNull(loginId)) {
            schoolResultDetail.setTotalRateUser(Arrays.stream(ArrayUtils.toPrimitive(allByRequestIdAndType.stream().filter(y -> y.getCreatedBy().equals(loginId.toString())).map(Rate::getRate).toArray(Double[]::new))).average().orElse(0));
        }
        schoolResultDetail.setTotalRate(Arrays.stream(ArrayUtils.toPrimitive(allByRequestIdAndType.stream().map(Rate::getRate).toArray(Double[]::new))).average().orElse(0));
        return schoolResultDetail;
    }

    @Override
    public School saveOrUpdate(SchoolRequest dto) throws IOException {

        School school = new School();
        BeanUtils.copyProperties(dto, school);
        School save = Objects.nonNull(dto.getId()) ? schoolRepository.findAllById(dto.getId()) : school;
        if (Objects.nonNull(dto.getId())) {
            if (Objects.nonNull(dto.getSchoolName())) {
                save.setIdUrl(getSearchableStringUrl(dto.getSchoolName(), schoolRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getSchoolName())).size()));
            }
        } else {
            save.setIdUrl(getSearchableStringUrl(dto.getSchoolName(), schoolRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getSchoolName())).size()));

        }
        if (Objects.nonNull(dto.getSchoolName())) {
            save.setSchoolName(dto.getSchoolName());
        }
        if (Objects.nonNull(dto.getContent())) {
            save.setContent(dto.getContent());
        }
        if (Objects.nonNull(dto.getWardId())) {
            save.setWardId(dto.getWardId()==0?null:dto.getWardId());
        }
        if (Objects.nonNull(dto.getDistrictId())) {
            save.setDistrictId(dto.getDistrictId()==0?null:dto.getDistrictId());
        }
        if (Objects.nonNull(dto.getProvinceId())) {
            save.setProvinceId(dto.getProvinceId()==0?null:dto.getProvinceId());
        }
        if (Objects.nonNull(dto.getType())) {
            save.setType(dto.getType());
        }
        if (Objects.nonNull(dto.getSummary())) {
            save.setSummary(dto.getSummary());
        }
        if (Objects.nonNull(dto.getAddress())) {
            save.setAddress(dto.getAddress());
        }
        if (Objects.nonNull(dto.getTotalStudent())) {
            save.setTotalStudent(dto.getTotalStudent());
        }
        if (Objects.nonNull(dto.getSchoolType())) {
            save.setSchoolType(dto.getSchoolType());
        }
        if (Objects.nonNull(dto.getSchoolTypeEducation())) {
            save.setSchoolTypeEducation(dto.getSchoolTypeEducation());
        }
        School result = schoolRepository.save(save);
        if (Objects.nonNull(dto.getFiles())) {

            attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), result.getId().intValue(), null, false);
        }
        return result;
    }

    @Override
    public void deleteById(Long id) {
        School schoolById = schoolRepository.findSchoolById(id);
        attachmentService.deleteAttachmentByRequestId(id.intValue(), SCHOOL_IMAGE.getName());
        rateRepository.deleteAll(rateRepository.findAllByRequestIdAndType(schoolById.getIdUrl(), SCHOOL_IMAGE.getName()));
        commentRepository.deleteAll(commentRepository.findAllByRequestIdAndTypeOrderByIdDesc(schoolById.getIdUrl(), SCHOOL_IMAGE.getName()));
        schoolRepository.deleteById(id);
    }

    @Override
    public List<School> saveSchools() {
        List<School> schools = CommonFunction.readSchoolsFromExcel();
        List<School> schoolOlds = schoolRepository.findAll();
        for (School school : schools) {
            if (schoolOlds.contains(school)) {
                return new ArrayList<>();
            }
        }
        return schoolRepository.saveAll(schools);
    }

    @Override
    public SchoolResultDto findAll(Pageable pageable, SchoolRequest dto) {

        validateDTO(dto);
        if(Objects.nonNull(dto.getPage())&&Objects.nonNull(dto.getSize())){
            pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by("id").descending());
        }

        if(Objects.nonNull(dto.getOrderType())) {
            if (dto.getOrderType() == 1) {
                // Đối với orderType = 1
                if ("desc".equalsIgnoreCase(dto.getOrder())) {
                    // Nếu order là "desc", sắp xếp theo totalRate giảm dần
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("totalRate").descending());
                } else if ("asc".equalsIgnoreCase(dto.getOrder())) {
                    // Nếu order là "asc", sắp xếp theo totalRate tăng dần
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("totalRate").ascending());
                }
            } else if (dto.getOrderType() == 2) {
                // Đối với orderType = 2
                if ("desc".equalsIgnoreCase(dto.getOrder())) {
                    // Nếu order là "desc", sắp xếp theo totalComment giảm dần
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("totalComment").descending());
                } else if ("asc".equalsIgnoreCase(dto.getOrder())) {
                    // Nếu order là "desc", sắp xếp theo totalComment tăng dần
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("totalComment").descending());
                }
            }
        }else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        }

        Page<SchoolResult> schools = schoolRepository.findAllBySchoolNameContainingIgnoreCaseQuerySearch(dto.getSearch(), dto.getWardId(), dto.getDistrictId(), dto.getProvinceId(), dto.getSchoolType(),dto.getSchoolTypeEducation(), pageable) ;

        SchoolResultDto schoolResultDto = new SchoolResultDto();
        List<SchoolResult> schoolsContent = schools.getContent();
        List<Integer> schoolIds = schoolsContent.stream().map(SchoolResult::getId).collect(Collectors.toList()).stream().map(Long::intValue).collect(Collectors.toList());
        List<String> schoolIUrl = schoolsContent.stream().map(SchoolResult::getIdUrl).collect(Collectors.toList());

        List<Attachment> allByRequestId = attachmentRepository.findAllByRequestIdInAndFileType(schoolIds, SCHOOL_IMAGE.getName());

        List<School> schools1 = new ArrayList<>();

        List<Rate> allByRequestIdInAndType = rateRepository.findAllByRequestIdInAndType(schoolIUrl, RateType.RATE_SCHOOL.getName());
        Map<String, Double> groupBy = allByRequestIdInAndType.stream().collect(Collectors.groupingBy(Rate::getRequestId, averagingDouble(Rate::getRate)));


        schoolsContent.forEach(x -> {
            School school = new School();
            BeanUtils.copyProperties(x, school);
            school.setUrl(allByRequestId.stream().filter(y->y.getRequestId().equals(x.getId().intValue())).findFirst().orElseThrow(NotFoundException::new).getUrl());
            for (Map.Entry<String, Double> entry : groupBy.entrySet()) {
                if (school.getId().toString().equals(entry.getKey())) {
                    school.setTotalRateUser(entry.getValue());
                    System.out.println(entry.getKey() + " " + entry.getValue());

                }
            }
            schools1.add(school);
        });


        PaginationModel paginationModel = new PaginationModel(schools.getPageable().getPageNumber(), schools.getPageable().getPageSize(), (int) schools.getTotalElements());
        schoolResultDto.setList(schools1);
        schoolResultDto.setPaginationModel(paginationModel);
        return schoolResultDto;
    }

    private static void validateDTO(SchoolRequest dto) {
        if(Objects.nonNull(dto.getSearch())&& dto.getSearch().isEmpty()){
            dto.setSearch(null);
        }
        if (Objects.nonNull(dto.getSchoolType()) && dto.getSchoolType().isEmpty()) {
            dto.setSchoolType(null);
        }
        if (Objects.nonNull(dto.getSchoolTypeEducation()) && dto.getSchoolTypeEducation().isEmpty()) {
            dto.setSchoolTypeEducation(null);
        }
        if (Objects.nonNull(dto.getProvinceId()) && dto.getProvinceId()==0) {
            dto.setProvinceId(null);
        }
        if (Objects.nonNull(dto.getWardId()) && dto.getWardId()==0) {
            dto.setWardId(null);
        }
        if (Objects.nonNull(dto.getDistrictId()) && dto.getDistrictId()==0) {
            dto.setDistrictId(null);
        }
    }


    @Override
    public List<SchoolType> getSchoolType(String type) {
        return schoolTypeRepository.findAllByType(type);
    }

}
