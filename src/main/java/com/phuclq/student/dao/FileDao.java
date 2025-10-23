package com.phuclq.student.dao;

import com.phuclq.student.common.Constants;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.*;
import com.phuclq.student.repository.UserHistoryFileRepository;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.FileType;
import com.phuclq.student.types.OrderFileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.phuclq.student.types.ActivityConstants.CARD;
import static com.phuclq.student.types.ActivityConstants.LIKE;

@Service
@Transactional
public class FileDao {

    private final Logger log = LoggerFactory.getLogger(FileDao.class);


    @Autowired
    private UserService userService;

    @Autowired
    private UserHistoryFileRepository userHistoryFileRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public FileResultDto myFile(FileHomePageRequest request, Pageable pageable) {
        List<Object> objList = null;

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        if (Objects.nonNull(request.getIsLike()) && request.getIsLike()) {
            sqlStatement.append("from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id left join attachment ab on u.id = ab.request_id and ab.file_type = " + "'" + FileType.USER_AVATAR.getName() + "' join user_history_file uhf on f.id = uhf.file_id  inner join user_history uh on uhf.user_hisoty_id = uh.id and uh.activity_id = 4 where f.is_deleted =0  ");
            sqlStatement.append("  and uh.user_id = ? ");
            listParam.add(userService.getUserLogin().getId());
        }
        if (Objects.nonNull(request.getIsCard()) && request.getIsCard()) {
            sqlStatement.append("from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id left join attachment ab on u.id = ab.request_id and ab.file_type = " + "'" + FileType.USER_AVATAR.getName() + "' join user_history_file uhf on f.id = uhf.file_id  inner join user_history uh on uhf.user_hisoty_id = uh.id and uh.activity_id = 8 where f.is_deleted =0  ");
            sqlStatement.append("  and uh.user_id = ? ");
            listParam.add(userService.getUserLogin().getId());
        }
        if (Objects.nonNull(request.getIsUser()) && request.getIsUser()) {
            sqlStatement.append("from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id left join attachment ab on u.id = ab.request_id and ab.file_type = " + "'" + FileType.USER_AVATAR.getName() + "' " + "where f.is_deleted =0  ");
            sqlStatement.append(" and f.author_id = ? ");
            listParam.add(userService.getUserLogin().getId());
        }
        if (Objects.nonNull(request.getIsDownload()) && request.getIsDownload()) {
            sqlStatement.append("from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id left join attachment ab on u.id = ab.request_id and ab.file_type = " + "'" + FileType.USER_AVATAR.getName() + "' join user_history_file uhf on f.id = uhf.file_id  inner join user_history uh on uhf.user_hisoty_id = uh.id and uh.activity_id = 1 where f.is_deleted =0  ");
            sqlStatement.append("  and uh.user_id = ? ");
            listParam.add(userService.getUserLogin().getId());

        }

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(f.title) like LOWER(?) ");
            sqlStatement.append(" or LOWER(i.value) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.user_name) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        if (Objects.nonNull(request.getPriceStart())) {
            sqlStatement.append(" and fp.price >= ? ");
            listParam.add(request.getPriceStart());
        }

        if (Objects.nonNull(request.getPriceEnd())) {
            sqlStatement.append(" and fp.price <= ? ");
            listParam.add(request.getPriceEnd());
        }
        if (Objects.nonNull(request.getIsVip()) && request.getIsVip()) {

            sqlStatement.append(" and f.is_vip = ? ");
            listParam.add(request.getIsVip());
        }
        if (Objects.nonNull(request.getIndustry())) {
            sqlStatement.append(" and f.industry_id = ? ");
            listParam.add(request.getIndustry());
        }
        if (Objects.nonNull(request.getSchool())) {

            sqlStatement.append(" and f.school_id = ? ");
            listParam.add(request.getSchool());
        }
        if (Objects.nonNull(request.getIsApprove()) && !request.getIsApprove()) {
            sqlStatement.append(" and f.approver_id is  null ");
        }

        if (Objects.nonNull(request.getIsApprove()) && request.getIsApprove()) {
            sqlStatement.append(" and f.approver_id is not null ");
        }
        if (request.getDateFrom() != null) {
            sqlStatement.append(" and f.CREATED_DATE >= ? ");
            listParam.add(request.getDateFrom());
        }
        if (request.getDateTo() != null) {
            sqlStatement.append(" and f.CREATED_DATE <= ? ");
            listParam.add(request.getDateTo());
        }

        if (Objects.nonNull(request.getOrderType())) {
            if (request.getOrderType().equals(OrderFileType.DOWNLOADS.getCode())) {
                sqlStatement.append(" order by f.dowloading " + request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.FAVORITES.getCode())) {
                sqlStatement.append(" order by f.total_like " + request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.PRICE.getCode())) {
                sqlStatement.append(" order by  fp.price " + request.getOrder());
            }
            if (request.getOrderType().equals(OrderFileType.VIEW.getCode())) {
                sqlStatement.append(" order by f.view " + request.getOrder());
            }
            sqlStatement.append(" ,case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc ");
        } else {
            sqlStatement.append(" order BY case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc ");
        }

        Query queryCount = entityManager.createNativeQuery(" select count(f.id) " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(pageable.getPageSize());
        listParam.add(pageable.getPageSize() * pageable.getPageNumber());
        Query query = entityManager.createNativeQuery(Constants.SQL_FILE + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<FileResult> list = new ArrayList<>();
        for (Object obj : objList) {
            FileResult result = new FileResult((Object[]) obj);
            list.add(result);
        }

        Page<FileResult> listCategory = new PageImpl<FileResult>(list, pageable, count);
        FileResultDto fileResultDto = new FileResultDto();
        User userLogin = userService.getUserLogin();
        List<UserHistoryDTO> fileHistoryHome = new ArrayList<>();
        if (Objects.nonNull(userLogin.getId())) {
            fileHistoryHome = userHistoryFileRepository.findFileHistoryHome(userLogin.getId());

        }
        List<UserHistoryDTO> finalFileHistoryHome = fileHistoryHome;
        listCategory.stream().parallel().forEach(x -> {

            if (finalFileHistoryHome.size() > 0) {
                List<UserHistoryDTO> collect = finalFileHistoryHome.stream().filter(f -> f.getFileId().equals(x.getId())).collect(Collectors.toList());
                if (collect.size() > 0) {
                    x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(LIKE)));
                    x.setIsCard(collect.stream().anyMatch(f -> f.getActivityId().equals(CARD)));
                }
            }
        });
        fileResultDto.setList(listCategory.getContent());
        PaginationModel paginationModel = new PaginationModel(listCategory.getPageable().getPageNumber(), listCategory.getPageable().getPageSize(), (int) listCategory.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;

    }

    public TotalMyDTO myFileTotal() {

        StringBuilder sqlStatement2 = new StringBuilder();
        StringBuilder sqlStatement3 = new StringBuilder();
        StringBuilder sqlStatementCard = new StringBuilder();
        String sqlStatement = "from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id join user_history_file uhf on f.id = uhf.file_id  inner join user_history uh on uhf.user_hisoty_id = uh.id and uh.activity_id = 4   " +
                " and uh.user_id =  " + userService.getUserLogin().getId() +
                " where f.is_deleted =0 ";

        sqlStatementCard.append("from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id join user_history_file uhf on f.id = uhf.file_id  inner join user_history uh on uhf.user_hisoty_id = uh.id and uh.activity_id = 8   ");
        sqlStatementCard.append(" and uh.user_id =  " + userService.getUserLogin().getId());
        sqlStatementCard.append(" where f.is_deleted =0 ");

        sqlStatement2.append("from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id ");
        sqlStatement2.append(" and f.author_id =  " + userService.getUserLogin().getId());
        sqlStatement2.append(" where f.is_deleted =0 ");
        sqlStatement3.append("from file f    inner join category c on f.category_id = c.id inner join file_price fp on f.id = fp.file_id " + "left join industry i on f.industry_id = i.id left join attachment a on f.id = a.request_id and a.file_type =" + "'" + FileType.FILE_AVATAR.getName() + "'" + " inner join user u on f.author_id = u.id join user_history_file uhf on f.id = uhf.file_id  inner join user_history uh on uhf.user_hisoty_id = uh.id and uh.activity_id = 1  ");
        sqlStatement3.append(" and uh.user_id =  " + userService.getUserLogin().getId());
        sqlStatement3.append(" where f.is_deleted =0 ");

        Integer queryCount = ((Number) entityManager.createNativeQuery(" select count(f.id) " + sqlStatement).getSingleResult()).intValue();
        Integer queryCount2 = ((Number) entityManager.createNativeQuery(" select count(f.id) " + sqlStatement2).getSingleResult()).intValue();
        Integer queryCount3 = ((Number) entityManager.createNativeQuery(" select count(f.id) " + sqlStatement3).getSingleResult()).intValue();
        Integer queryCountCard = ((Number) entityManager.createNativeQuery(" select count(f.id) " + sqlStatementCard).getSingleResult()).intValue();
        TotalMyDTO totalMyDTO = new TotalMyDTO();
        totalMyDTO.setIsUser(queryCount2);
        totalMyDTO.setIsLike(queryCount);
        totalMyDTO.setIsDownload(queryCount3);

        totalMyDTO.setIsCard(queryCountCard);
        return totalMyDTO;


    }


}
