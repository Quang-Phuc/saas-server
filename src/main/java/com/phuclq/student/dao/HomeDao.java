package com.phuclq.student.dao;

import com.phuclq.student.common.Constants;
import com.phuclq.student.domain.UserHistoryHome;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.home.HomeRequest;
import com.phuclq.student.dto.job.HomeResult;
import com.phuclq.student.dto.job.HomeResultDto;
import com.phuclq.student.repository.UserHistoryHomeRepository;
import com.phuclq.student.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static com.phuclq.student.types.ActivityConstants.*;

@Service
@Transactional
public class HomeDao {

    private final Logger log = LoggerFactory.getLogger(HomeDao.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserHistoryHomeRepository userHistoryHomeRepository;

    @Value("${file.url.home.find}")
    private String fileUrlHomeFind;

    @PersistenceContext
    private EntityManager entityManager;


    public String sqlHome(Integer loginId, String type, Integer activity) {
        return "from home j " +
                "         join province p on j.province_id = p.id\n" +
                "         join district d on j.district_id = d.id\n" +
                "         join ward w on j.ward_id = w.id\n" +
                "         join user_history_home uhh on j.id = uhh.home_id and uhh.created_by = " + loginId +
                "         join (select id, request_id, url\n" +
                "               from attachment\n" +
                "               where id in (select max(id)\n" +
                "                            from attachment at\n" +
                "                            where at.file_type = " + "'" + type + "' " +
                "                            group by request_id)) a on j.id = a.request_id where j.is_deleted =0  and uhh.activity_id = " + activity + " and type =" + "'" + type + "' ";
    }

    public HomeResultDto myHome(HomeRequest request, Pageable pageable) {
        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();
        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(sqlHome(loginId, request.getType(), request.getActivityId())
        );
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
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(0)) {
            sqlStatement.append(" and j.approver_id is  null ");
        }


        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(1)) {
            sqlStatement.append(" and j.approver_id is not null ");
        }
        if (request.getDateFrom() != null) {
            sqlStatement.append(" and j.CREATED_DATE >= ? ");
            listParam.add(request.getDateFrom());
        }
        if (request.getDateTo() != null) {
            sqlStatement.append(" and j.CREATED_DATE <= ? ");
            listParam.add(request.getDateTo().plusDays(2));
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

    private void setLikeAndCard(List<UserHistoryHome> finalFileHistoryHome, HomeResult x) {
        if (!finalFileHistoryHome.isEmpty()) {
            List<UserHistoryHome> collect = finalFileHistoryHome.stream().filter(f -> f.getHomeId().toString().equals(x.getId().toString())).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(LIKE_HOME)));
                x.setIsCard(collect.stream().anyMatch(f -> f.getActivityId().equals(CARD_HOME)));
            }
        }

    }


    public TotalMyDTO myFileTotal(String request) {

        StringBuilder isLikeSql = new StringBuilder();
        StringBuilder byUserSql = new StringBuilder();
        StringBuilder isCardSql = new StringBuilder();
        Integer loginId = userService.getUserLogin().getId();
        isLikeSql.append(sqlHome(loginId, request, LIKE_HOME));
        byUserSql.append(sqlHome(loginId, request, UPLOAD_HOME));
        isCardSql.append(sqlHome(loginId, request, CARD_HOME));


        Integer isLike = ((Number) entityManager.createNativeQuery(" select count(j.id) " + isLikeSql).getSingleResult()).intValue();
        ;
        Integer byUser = ((Number) entityManager.createNativeQuery(" select count(j.id) " + byUserSql).getSingleResult()).intValue();
        ;
        Integer isCard = ((Number) entityManager.createNativeQuery(" select count(j.id) " + isCardSql).getSingleResult()).intValue();
        TotalMyDTO totalMyDTO = new TotalMyDTO();
        totalMyDTO.setIsUser(byUser);
        totalMyDTO.setIsLike(isLike);
        totalMyDTO.setIsCard(isCard);
        return totalMyDTO;


    }


}
