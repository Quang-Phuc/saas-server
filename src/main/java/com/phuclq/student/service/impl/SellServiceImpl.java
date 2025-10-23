package com.phuclq.student.service.impl;

import com.phuclq.student.common.Constants;
import com.phuclq.student.dao.SellDao;
import com.phuclq.student.domain.*;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.sell.*;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.NotFoundException;
import com.phuclq.student.repository.*;
import com.phuclq.student.security.AuthoritiesConstants;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.SellService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.ActivityConstants;
import com.phuclq.student.types.NotificationType;
import com.phuclq.student.types.StatusType;
import com.phuclq.student.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.phuclq.student.types.ActivityConstants.LIKE_SELL;
import static com.phuclq.student.types.ActivityConstants.UPLOAD_BLOG;
import static com.phuclq.student.types.FileType.FILE_BLOG;
import static com.phuclq.student.types.FileType.SELL_IMAGE;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellServiceImpl implements SellService {


    private final SellDao sellDao;

    private final AttachmentRepository attachmentRepository;

    private final AttachmentService attachmentService;

    private final UserService userService;

    private final SellRepository sellRepository;

    private final UserHistorySellRepository userHistorySellRepository;

    private final SellCategoryRepository sellCategoryRepository;

    private final FCMService fcmService;

    private final UserRoleRepository userRoleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long creatOrUpdate(SellRequest dto) throws IOException {

        Sell sell = new Sell();
        BeanUtils.copyProperties(dto, sell);
        Sell save = Objects.nonNull(dto.getId()) ? sellRepository.findById(dto.getId()).get() : sell;
        if (Objects.nonNull(dto.getId())) {
            if (Objects.nonNull(dto.getTitle())) {
                save.setIdUrl(getSearchableStringUrl(dto.getTitle(), sellRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getTitle())).size()));
            }
        } else {
            save.setIdUrl(getSearchableStringUrl(dto.getTitle(), sellRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getTitle())).size()));

        }
        if (Objects.nonNull(dto.getPhone())) {
            save.setPhone(dto.getPhone());
        }
        if (Objects.nonNull(dto.getSellCategoryId())) {
            save.setSellCategoryId(dto.getSellCategoryId());
        }
        if (Objects.nonNull(dto.getWardId())) {
            save.setWardId(dto.getWardId());
        }
        if (Objects.nonNull(dto.getDistrictId())) {
            save.setDistrictId(dto.getDistrictId());
        }
        if (Objects.nonNull(dto.getProvinceId())) {
            save.setProvinceId(dto.getProvinceId());
        }
        if (Objects.nonNull(dto.getContent())) {
            save.setContent(dto.getContent());
        }
        if (Objects.nonNull(dto.getTitle())) {
            save.setTitle(dto.getTitle());
        }
        Sell result = sellRepository.save(save);


        if (Objects.isNull(dto.getId())) {
            activityHome(result.getId(), ActivityConstants.UPLOAD_SELL);
        }
        if (Objects.nonNull(dto.getFiles())) {

            attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), result.getId().intValue(), null, false);
        }
        return result.getId();
    }

    @Override
    public Page<SellCategory> search(Pageable pageable, String search) {
        return !Objects.requireNonNull(search).isEmpty() ? sellCategoryRepository.findAllByNameContainingIgnoreCase(search.trim(), pageable) : sellCategoryRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        attachmentService.deleteAttachmentByRequestId(id.intValue(), SELL_IMAGE.getName());
        sellRepository.deleteById(id);
    }

    @Override
    public SellResultDto findAllById(String idUrlCategory, String idUrl) {
        SellResultDto resultDto = new SellResultDto();
        SellDTOIn sellWithDetailsByIdUrl = sellRepository.findSellWithDetailsByIdUrl(idUrl);
        SellDTO sell =processSellDTO(sellWithDetailsByIdUrl);
        List<Attachment> attachmentByRequestIdAndType = attachmentService.getAttachmentByRequestIdAndType(sell.getId().intValue(), SELL_IMAGE.getName());

        resultDto.setSell(sell);
        resultDto.setUrls(attachmentByRequestIdAndType.stream().map(Attachment::getUrl).collect(Collectors.toList()));
        return resultDto;
    }

    public SellDTO processSellDTO(SellDTOIn sellDTO) {
        SellDTO sellDTOs = new SellDTO();
        sellDTOs.setId(sellDTO.getId());
        sellDTOs.setSellCategoryId(sellDTO.getSellCategoryId());
        sellDTOs.setWardId(sellDTO.getWardId());
        sellDTOs.setDistrictId(sellDTO.getDistrictId());
        sellDTOs.setProvinceId(sellDTO.getProvinceId());
        sellDTOs.setContent(sellDTO.getContent());
        sellDTOs.setTitle(sellDTO.getTitle());
        sellDTOs.setApproverId(sellDTO.getApproverId());
        sellDTOs.setIsDeleted(sellDTO.getIsDeleted());
        sellDTOs.setMoneyTop(sellDTO.getMoneyTop());
        sellDTOs.setStartMoneyTop(sellDTO.getStartMoneyTop());
        sellDTOs.setEndMoneyTop(sellDTO.getEndMoneyTop());
        sellDTOs.setTotalCard(sellDTO.getTotalCard());
        sellDTOs.setDeleteId(sellDTO.getDeleteId());

        // Additional fields
        sellDTOs.setCategoryId(sellDTO.getCategoryId());
        sellDTOs.setCategoryName(sellDTO.getCategoryName());

        sellDTOs.setDistrictName(sellDTO.getDistrictName());
        sellDTOs.setDistrictPrefix(sellDTO.getDistrictPrefix());
        sellDTOs.setDistrictProvinceId(sellDTO.getDistrictProvinceId());

        sellDTOs.setWardName(sellDTO.getWardName());
        sellDTOs.setWardPrefix(sellDTO.getWardPrefix());
        sellDTOs.setWardProvinceId(sellDTO.getWardProvinceId());
        sellDTOs.setWardDistrictId(sellDTO.getWardDistrictId());

        sellDTOs.setProvinceName(sellDTO.getProvinceName());
        sellDTOs.setProvinceCode(sellDTO.getProvinceCode());

        sellDTOs.setPrice(sellDTO.getPrice());
        sellDTOs.setQuantity(sellDTO.getQuantity());

        sellDTOs.setUserName(sellDTO.getUserName());
        sellDTOs.setPhone(sellDTO.getPhone());
        sellDTOs.setCreatedDate(DateTimeUtils.convertDateTimeToString(sellDTO.getCreatedDate(),DateTimeUtils.ddMMyyyy));


        return sellDTOs;
    }


    @Override
    public UserHistorySell activityHome(Long id, Integer activity) {
        UserHistorySell userHistoryHome = new UserHistorySell();
        userHistoryHome.setActivityId(activity);
        userHistoryHome.setSellId(id);
        return userHistorySellRepository.save(userHistoryHome);
    }


    @Override
    public void deleteActivityHome(Long id, Integer card) {
        UserHistorySell allByCreatedByAndActivityIdAndHomeId = userHistorySellRepository.findAllByCreatedByAndActivityIdAndSellId(userService.getUserLogin().getId().toString(), card, id);
        if (Objects.isNull(allByCreatedByAndActivityIdAndHomeId)) {
            throw new BusinessHandleException("SS000");
        }
        userHistorySellRepository.delete(allByCreatedByAndActivityIdAndHomeId);
    }

    @Override
    public SellResultSearchDto searchSell(Boolean admin, Pageable pageable, SellRequest request) {
        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(Constants.SQL_SELL_JOIN);
        if (!admin) {
            sqlStatement.append(" and approver_id is not null ");

        } else {
            if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(0)) {
                sqlStatement.append(" and j.approver_id is  null ");
            }
            if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(1)) {
                sqlStatement.append(" and j.approver_id is not  null ");
            }
        }
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
        if (request.getDateFrom() != null) {
            sqlStatement.append(" and j.CREATED_DATE >= ? ");
            listParam.add(request.getDateFrom());
        }
        if (request.getDateTo() != null) {
            sqlStatement.append(" and j.CREATED_DATE <= ? ");
            listParam.add(request.getDateTo());
        }

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(j.title) like LOWER(?) ");
            sqlStatement.append(" or LOWER( j.content) like LOWER(?) ");
            sqlStatement.append(" or LOWER(p._name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(d._name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(w._name) like LOWER(?)) ");
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
        Query query = entityManager.createNativeQuery(Constants.SQL_SELL + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<SellResult> list = new ArrayList<>();
        for (Object obj : objList) {
            SellResult result = new SellResult((Object[]) obj);
            list.add(result);
        }

        Page<SellResult> homeResults = new PageImpl<SellResult>(list, pageable, count);
        SellResultSearchDto fileResultDto = new SellResultSearchDto();
        List<UserHistorySell> userHistoryHomes = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            userHistoryHomes = userHistorySellRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistorySell> userHistoryHomeList = userHistoryHomes;
        homeResults.stream().parallel().forEach(x -> {
            setLikeAndCard(userHistoryHomeList, x);
        });
        fileResultDto.setList(homeResults.getContent());
        PaginationModel paginationModel = new PaginationModel(homeResults.getPageable().getPageNumber(), homeResults.getPageable().getPageSize(), (int) homeResults.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    private void setLikeAndCard(List<UserHistorySell> finalFileHistoryHome, SellResult x) {
        if (!finalFileHistoryHome.isEmpty()) {
            List<UserHistorySell> collect = finalFileHistoryHome.stream().filter(f -> f.getSellId().toString().equals(x.getId().toString())).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(LIKE_SELL)));
            }
        }

    }

    @Override
    public SellResultSearchDto myHome(SellRequest request, Pageable pageable) {
        return sellDao.myHome(request, pageable);
    }

    @Override
    public TotalMyDTO total() {
        return sellDao.myFileTotal();
    }

    @Override
    public void approve(Long id) {
        Sell sell = sellRepository.findById(id).orElseThrow(NotFoundException::new);
        sell.setApproverId(userService.getUserLogin().getId());
        sell.setApprovedDate(DateTimeUtils.timeNow());
        sellRepository.save(sell);
        fcmService.tokenFireBase(NotificationType.BLOG_ACCEPTED.getTitle(), NotificationType.BLOG_ACCEPTED.getMessage(), new Notification(NotificationType.BLOG_ACCEPTED.getCode(), sell.getCreatedBy(), NotificationType.BLOG_ACCEPTED.getType(), StatusType.DONE.getName(), NotificationType.HOME_ACCEPTED.getMessage(), NotificationType.BLOG_ACCEPTED.getImageIcon(), NotificationType.BLOG_ACCEPTED.getUrlDetail()));

    }

    @Override
    public Page<HistoryFileResult> deleteHistory(SellRequest request) {
        User user = userService.getUserLogin();
        List<UserHistorySell> userHistoryBlogList = new ArrayList<>();
        if (userRoleRepository.findByRole(AuthoritiesConstants.ADMIN).get().getId().equals(user.getRoleId())) {
            userHistoryBlogList = userHistorySellRepository.findAllByActivityIdAndSellIdIn(request.getActivityId(), request.getIds());
        } else {
            userHistoryBlogList = userHistorySellRepository.findAllByActivityIdAndSellIdInAndCreatedBy(request.getActivityId(), request.getIds(), user.getId().toString());
        }

        userHistorySellRepository.deleteAll(userHistoryBlogList);
        List<Sell> allByIdIn = sellRepository.findAllByIdIn(request.getIds());
        allByIdIn.forEach(y -> {
            if (UPLOAD_BLOG.equals(request.getActivityId())) {
                y.setIsDeleted(true);
                y.setDeleteId(user.getId());
                Instant instant = Instant.now();
                Timestamp timestamp = Timestamp.from(instant);
                y.setDeleteDate(timestamp);
                List<String> typeFiles = Collections.singletonList(FILE_BLOG.getName());
                attachmentRepository.deleteAll(attachmentRepository.findAllByRequestIdAndFileTypeIn(y.getId().intValue(), typeFiles));
            }
        });
        sellRepository.saveAll(allByIdIn);


        return null;
    }

}
