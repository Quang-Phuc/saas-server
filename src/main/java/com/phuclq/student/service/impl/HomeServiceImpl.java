package com.phuclq.student.service.impl;

import com.phuclq.student.common.Constants;
import com.phuclq.student.dao.HomeDao;
import com.phuclq.student.domain.*;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.HomeDetailDTO;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.home.HomeRequest;
import com.phuclq.student.dto.job.HomeResult;
import com.phuclq.student.dto.job.HomeResultDto;
import com.phuclq.student.dto.webhook.HomeDto;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.exception.NotFoundException;
import com.phuclq.student.repository.*;
import com.phuclq.student.security.AuthoritiesConstants;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.HomeService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.NotificationType;
import com.phuclq.student.types.StatusType;
import com.phuclq.student.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.phuclq.student.types.ActivityConstants.*;
import static com.phuclq.student.types.FileType.HOME_SHARE;
import static com.phuclq.student.utils.StringUtils.*;

@Service
public class HomeServiceImpl implements HomeService {

    private static final Logger log = LoggerFactory.getLogger(HomeServiceImpl.class);
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    HomeDao homeDao;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private UserHistoryHomeRepository userHistoryHomeRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private FCMService fcmService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Value("${file.url.home.find}")
    private String fileUrlHomeFind;


    @Override
    public Long create(HomeRequest dto) throws IOException {
        Integer login = userService.getUserLogin().getId();

        try{
        if (Objects.isNull(dto.getId())) {
            Home home = new Home();
            BeanUtils.copyProperties(dto, home);
            home.setIsDeleted(false);
            home.setIdUrl(getSearchableStringUrl(dto.getName(), homeRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));

            Home home1 = homeRepository.save(home);
            if (Objects.nonNull(dto.getFiles())) {
                attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), home1.getId().intValue(), login, false);
            }
            Long id = home1.getId();

            activityHome(id, UPLOAD_HOME);

            return id;
        } else {
            Home allById = homeRepository.findById(dto.getId()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));

            if (Objects.nonNull(dto.getTitle())) {
                allById.setIdUrl(getSearchableStringUrl(dto.getName(), homeRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));
            }
            if (Objects.nonNull(dto.getTitle())) {
                allById.setTitle(dto.getTitle());
            }
            if (Objects.nonNull(dto.getWardId())) {
                allById.setWardId(dto.getWardId());
            }
            if (Objects.nonNull(dto.getDistrictId())) {
                allById.setDistrictId(dto.getDistrictId());
            }
            if (Objects.nonNull(dto.getProvinceId())) {
                allById.setProvinceId(dto.getProvinceId());
            }
            if (Objects.nonNull(dto.getPrice())) {
                allById.setPrice(dto.getPrice());
            }
            if (Objects.nonNull(dto.getAddress())) {
                allById.setAddress(dto.getAddress());
            }
            if (Objects.nonNull(dto.getAcreage())) {
                allById.setAcreage(dto.getAcreage());
            }
            if (Objects.nonNull(dto.getClosed())) {
                allById.setClosed(dto.getClosed());
            }
            if (Objects.nonNull(dto.getShared())) {
                allById.setShared(dto.getShared());
            }
            if (Objects.nonNull(dto.getContent())) {
                allById.setContent(dto.getContent());
            }
            if (Objects.nonNull(dto.getPhone())) {
                allById.setPhone(dto.getPhone());
            }
            if (Objects.nonNull(dto.getEmail())) {
                allById.setEmail(dto.getEmail());
            }
            if (Objects.nonNull(dto.getAirCondition())) {
                allById.setAirCondition(dto.getAirCondition());
            }
            if (Objects.nonNull(dto.getFridge())) {
                allById.setFridge(dto.getFridge());
            }
            if (Objects.nonNull(dto.getWashingMachine())) {
                allById.setWashingMachine(dto.getWashingMachine());
            }
            if (Objects.nonNull(dto.getName())) {
                allById.setName(dto.getName());
            }
            if (Objects.nonNull(dto.getNameUser())) {
                allById.setNameUser(dto.getNameUser());

            }
            allById.setApprovedDate(null);
            allById.setApproverId(null);
            if (Objects.nonNull(dto.getFiles())) {
                attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), allById.getId().intValue(), login, false);
            }
            Home home1 = homeRepository.save(allById);
            return home1.getId();
        }
        } catch (Exception e) {
            log.error("Error when create home{}", convertObjectToJson(dto));
            throw new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST);
        }

    }

    @Override
    public HomeResultDto search(HomeRequest request, Pageable pageable) {

        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(Constants.SQL_HOME_JOIN);
        sqlStatement.append(" and approver_id is not null ");
        if (Objects.nonNull(request.getProvinceId())) {
            sqlStatement.append(" and j.province_id = ? ");
            listParam.add(request.getProvinceId());
        }
        if (Objects.nonNull(request.getDistrictId())) {
            sqlStatement.append(" and j.district_id = ? ");
            listParam.add(request.getDistrictId());
        }
        if (Objects.nonNull(request.getWardId())) {
            sqlStatement.append(" and j.ward_id = ? ");
            listParam.add(request.getWardId());
        }
        if (Objects.nonNull(request.getType())) {
            sqlStatement.append(" and j.type = ? ");
            listParam.add(request.getType());
        }
        if (Objects.nonNull(request.getClosed())) {
            sqlStatement.append(" and j.CLOSED = ? ");
            listParam.add(request.getClosed());
        }
        if (Objects.nonNull(request.getShared())) {
            sqlStatement.append(" and j.SHARED = ? ");
            listParam.add(request.getShared());
        }

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(j.NAME_USER) like LOWER(?) ");
            sqlStatement.append(" or LOWER( j.name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.phone) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.TITLE) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.address) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        sqlStatement.append(" order BY case when j.start_money_top is null then 1 else 0 end,j.start_money_top,j.id desc ");

        Query queryCount = entityManager.createNativeQuery(" select count(j.id) " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(pageable.getPageSize());
        listParam.add(pageable.getPageSize() * pageable.getPageNumber());
        Query query = entityManager.createNativeQuery(Constants.SQL_HOME + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<HomeResult> list = new ArrayList<>();
        for (Object obj : objList) {
            HomeResult result = new HomeResult((Object[]) obj);
            if (Objects.isNull(result.getUrl())) {
                result.setUrl(fileUrlHomeFind);
            }
            list.add(result);
        }

        Page<HomeResult> homeResults = new PageImpl<HomeResult>(list, pageable, count);
        HomeResultDto fileResultDto = new HomeResultDto();
        List<UserHistoryHome> userHistoryHomes = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            userHistoryHomes = userHistoryHomeRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistoryHome> userHistoryHomeList = userHistoryHomes;
        homeResults.stream().parallel().forEach(x -> {
            setLikeAndCard(userHistoryHomeList, x);
        });
        fileResultDto.setList(homeResults.getContent());
        PaginationModel paginationModel = new PaginationModel(homeResults.getPageable().getPageNumber(), homeResults.getPageable().getPageSize(), (int) homeResults.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    @Override
    public HomeResultDto searchAdmin(HomeRequest request, Pageable pageable) {
        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(Constants.SQL_HOME_JOIN);

        if (Objects.nonNull(request.getProvinceId())) {
            sqlStatement.append(" and j.province_id = ? ");
            listParam.add(request.getProvinceId());
        }
        if (Objects.nonNull(request.getDistrictId())) {
            sqlStatement.append(" and j.district_id = ? ");
            listParam.add(request.getDistrictId());
        }
        if (Objects.nonNull(request.getType())) {
            sqlStatement.append(" and j.type = ? ");
            listParam.add(request.getType());
        }
        if (Objects.nonNull(request.getWardId())) {
            sqlStatement.append(" and j.ward_id = ? ");
            listParam.add(request.getWardId());
        }
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(0)) {
            sqlStatement.append(" and j.approver_id is  null ");
        }

        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(1)) {
            sqlStatement.append(" and j.approver_id is not null ");
        }
        if (Objects.nonNull(request.getClosed())) {
            sqlStatement.append(" and j.CLOSED = ? ");
            listParam.add(request.getClosed());
        }
        if (Objects.nonNull(request.getShared())) {
            sqlStatement.append(" and j.SHARED = ? ");
            listParam.add(request.getShared());
        }

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(j.NAME_USER) like LOWER(?) ");
            sqlStatement.append(" or LOWER( j.name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.phone) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.TITLE) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.address) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        sqlStatement.append(" order BY case when j.start_money_top is null then 1 else 0 end,j.start_money_top,j.id desc ");

        Query queryCount = entityManager.createNativeQuery(" select count(j.id) " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(pageable.getPageSize());
        listParam.add(pageable.getPageSize() * pageable.getPageNumber());
        Query query = entityManager.createNativeQuery(Constants.SQL_HOME + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<HomeResult> list = new ArrayList<>();
        for (Object obj : objList) {
            HomeResult result = new HomeResult((Object[]) obj);
            if (Objects.isNull(result.getUrl())) {
                result.setUrl(fileUrlHomeFind);
            }
            list.add(result);
        }

        Page<HomeResult> homeResults = new PageImpl<HomeResult>(list, pageable, count);
        HomeResultDto fileResultDto = new HomeResultDto();

        List<Integer> collect = homeResults.stream().map(HomeResult::getId).collect(Collectors.toList()).stream().map(BigInteger::intValue).collect(Collectors.toList());
        List<Attachment> allByRequestIdInAndFileType = attachmentRepository.findAllByRequestIdInAndFileType(collect, request.getType());
        homeResults.stream().parallel().forEach(x -> {
            x.setUrls(allByRequestIdInAndFileType.stream().filter(i -> i.getRequestId().equals(x.getId().intValue())).map(Attachment::getUrl).collect(Collectors.toList()));
        });
        fileResultDto.setList(homeResults.getContent());
        PaginationModel paginationModel = new PaginationModel(homeResults.getPageable().getPageNumber(), homeResults.getPageable().getPageSize(), (int) homeResults.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    @Override
    public HomeResultDto topSame(HomeRequest request, Pageable pageable) {

        Home allById = homeRepository.findById(request.getId()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
        request.setProvinceId(allById.getProvinceId());
        request.setDistrictId(allById.getDistrictId());
        request.setWardId(allById.getWardId());

        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(Constants.SQL_HOME + Constants.SQL_HOME_JOIN);
        if (Objects.nonNull(request.getType())) {
            sqlStatement.append(" and  j.type = ? ");
            listParam.add(request.getTitle());
        }
        if (Objects.nonNull(request.getId())) {
            sqlStatement.append(" and  j.id != ? ");
            listParam.add(request.getId());
        }
        sqlStatement.append(" and ( ");
        if (Objects.nonNull(request.getProvinceId())) {
            sqlStatement.append("  j.province_id = ? ");
            listParam.add(request.getProvinceId());
        }

        if (Objects.nonNull(request.getDistrictId())) {
            sqlStatement.append(" or j.district_id = ? ");
            listParam.add(request.getDistrictId());
        }

        sqlStatement.append(" ) ");
        sqlStatement.append(" union ");
        sqlStatement.append(Constants.SQL_HOME + Constants.SQL_HOME_JOIN);
        sqlStatement.append(" ) j ");


        Query queryCount = entityManager.createNativeQuery(" select count(*) from ( " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(pageable.getPageSize());
        listParam.add(pageable.getPageSize() * pageable.getPageNumber());
        Query query = entityManager.createNativeQuery(Constants.SQL_HOME_TOP + " from ( " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<HomeResult> list = new ArrayList<>();
        for (Object obj : objList) {
            HomeResult result = new HomeResult((Object[]) obj);
            if (Objects.isNull(result.getUrl())) {
                result.setUrl(fileUrlHomeFind);
            }
            list.add(result);
        }

        Page<HomeResult> file = new PageImpl<HomeResult>(list, pageable, count);
        HomeResultDto fileResultDto = new HomeResultDto();
        List<UserHistoryHome> fileHistoryHome = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            fileHistoryHome = userHistoryHomeRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistoryHome> finalFileHistoryHome = fileHistoryHome;
        file.stream().parallel().forEach(x -> {
            setLikeAndCard(finalFileHistoryHome, x);
        });
        fileResultDto.setList(file.getContent());
        PaginationModel paginationModel = new PaginationModel(file.getPageable().getPageNumber(), file.getPageable().getPageSize(), (int) file.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    @Override
    public void approveHome(Long id) {
        Home home = homeRepository.findById(id).orElseThrow(NotFoundException::new);
        home.setApproverId(userService.getUserLogin().getId());
        home.setApprovedDate(DateTimeUtils.timeNow());
        homeRepository.save(home);
        fcmService.tokenFireBase(NotificationType.HOME_ACCEPTED.getTitle(), NotificationType.HOME_ACCEPTED.getMessage(), new Notification(NotificationType.HOME_ACCEPTED.getCode(), home.getCreatedBy(), NotificationType.HOME_ACCEPTED.getType(), StatusType.DONE.getName(), NotificationType.HOME_ACCEPTED.getMessage(), NotificationType.HOME_ACCEPTED.getImageIcon(), NotificationType.HOME_ACCEPTED.getUrlDetail()));

    }

    @Override
    public HomeDto findAllById(String id) {
        HomeDetailDTO allById = homeRepository.findAllById(id);
        List<Attachment> allByRequestId = attachmentRepository.findAllByRequestIdAndFileType(allById.getId().intValue(), HOME_SHARE.getName());
        HomeDto homeDto = new HomeDto();
        homeDto.setAllById(allById);
        if (allByRequestId.isEmpty()) {
            Attachment attachment = new Attachment();
            attachment.setUrl(fileUrlHomeFind);
            allByRequestId.add(attachment);

        }
        homeDto.setAttachments(allByRequestId);
        return homeDto;
    }

    @Override
    public void deleteById(Long id) {
        homeRepository.deleteById(id);
    }

    private void setLikeAndCard(List<UserHistoryHome> finalFileHistoryHome, HomeResult x) {
        if (!finalFileHistoryHome.isEmpty()) {
            List<UserHistoryHome> collect = finalFileHistoryHome.stream().filter(f -> f.getHomeId().toString().equals(x.getId().toString())).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(LIKE_HOME)));
                x.setIsCard(collect.stream().anyMatch(f -> f.getActivityId().equals(CARD_HOME)));
            }
        }

    }

    @Override
    public UserHistoryHome activityHome(Long id, Integer activity) {

        UserHistoryHome userHistoryHome = new UserHistoryHome();
        userHistoryHome.setActivityId(activity);
        userHistoryHome.setHomeId(id);
        return userHistoryHomeRepository.save(userHistoryHome);
    }

    @Override
    public void deleteActivityHome(Long id, Integer card) {
        UserHistoryHome allByCreatedByAndActivityIdAndHomeId = userHistoryHomeRepository.findAllByCreatedByAndActivityIdAndHomeId(userService.getUserLogin().getId().toString(), card, id);
        if (Objects.isNull(allByCreatedByAndActivityIdAndHomeId)) {
            throw new BusinessHandleException("SS000");
        }
        userHistoryHomeRepository.delete(allByCreatedByAndActivityIdAndHomeId);
    }

    @Override
    public HomeResultDto myHome(HomeRequest request, Pageable pageable) {
        return homeDao.myHome(request, pageable);
    }

    @Override
    public TotalMyDTO total(String type) {
        return homeDao.myFileTotal(type);
    }

    @Override
    public Page<HistoryFileResult> deleteHistory(HomeRequest request) {

        User user = userService.getUserLogin();
        if (userRoleRepository.findByRole(AuthoritiesConstants.ADMIN).get().getId().equals(user.getRoleId())) {
            userHistoryHomeRepository.deleteAll(userHistoryHomeRepository.findAllByActivityIdAndHomeIdIn(request.getActivityId(), request.getIds()));
        } else {
            userHistoryHomeRepository.deleteAll(userHistoryHomeRepository.findAllByCreatedByAndActivityIdAndHomeIdIn(user.getId().toString(), request.getActivityId(), request.getIds()));
        }

        request.getIds().forEach(y -> {
            if (UPLOAD_HOME.equals(request.getActivityId())) {
                Home home = homeRepository.findById(y).get();
                home.setIsDeleted(true);
                home.setDeletedId(user.getId());
                Instant instant = Instant.now();
                Timestamp timestamp = Timestamp.from(instant);
                home.setDeleteDate(timestamp);
                attachmentRepository.deleteAll(attachmentRepository.findAllByRequestIdAndFileTypeIn(y.intValue(), Collections.singletonList(request.getType())));
                homeRepository.save(home);
            }
        });


        return null;
    }

}

