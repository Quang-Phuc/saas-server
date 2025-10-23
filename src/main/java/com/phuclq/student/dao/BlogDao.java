package com.phuclq.student.dao;

import com.phuclq.student.common.Constants;
import com.phuclq.student.domain.UserHistoryHome;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.blog.BlogRequest;
import com.phuclq.student.dto.blog.BlogResult;
import com.phuclq.student.dto.blog.BlogResultDto;
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

import static com.phuclq.student.types.ActivityConstants.LIKE_BLOG;
import static com.phuclq.student.types.ActivityConstants.UPLOAD_BLOG;
import static com.phuclq.student.types.FileType.FILE_BLOG;

@Service
@Transactional
public class BlogDao {

    private final Logger log = LoggerFactory.getLogger(BlogDao.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserHistoryHomeRepository userHistoryHomeRepository;

    @Value("${file.url.home.find}")
    private String fileUrlHomeFind;

    @PersistenceContext
    private EntityManager entityManager;


    public String sqlHome(Integer loginId, Integer activity) {
        String type = FILE_BLOG.getName();
        return "from blog j " +
                "                     join category_blog w on j.category_blog_id = w.id\n" +
                "                       join user u on j.created_by = u.id \n" +
                "         join user_history_blog uhh on j.id = uhh.blog_id and uhh.created_by = " + loginId +
                "         left join (select id, request_id, url\n" +
                "               from attachment\n" +
                "               where id in (select max(id)\n" +
                "                            from attachment at\n" +
                "                            where at.file_type = " + "'" + type + "' " +
                "                            group by request_id)) a on j.id = a.request_id where j.is_deleted =0  and uhh.activity_id = " + activity + "  ";
    }

    public BlogResultDto myHome(BlogRequest request, Pageable pageable) {
        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();
        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(sqlHome(loginId, request.getActivityId())
        );
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(0)) {
            sqlStatement.append(" and j.approver_id is  null ");
        }

        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(1)) {
            sqlStatement.append(" and j.approver_id is not null ");
        }
        if (Objects.nonNull(request.getCategoryBlogId())) {
            sqlStatement.append(" and j.category_blog_id = ? ");
            listParam.add(request.getCategoryBlogId());
        }


        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(j.title) like LOWER(?) ");
            sqlStatement.append(" or LOWER( u.email) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.user_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.full_name) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        sqlStatement.append(" order BY j.id desc ");

        Query queryCount = entityManager.createNativeQuery(" select count(j.id) " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(pageable.getPageSize());
        listParam.add(pageable.getPageSize() * pageable.getPageNumber());
        Query query = entityManager.createNativeQuery(Constants.SQL_BLOG + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<BlogResult> list = new ArrayList<>();
        for (Object obj : objList) {
            BlogResult result = new BlogResult((Object[]) obj);
            if (Objects.isNull(result.getUrl())) {
                result.setUrl(fileUrlHomeFind);
            }
            list.add(result);
        }

        Page<BlogResult> BlogResults = new PageImpl<BlogResult>(list, pageable, count);
        BlogResultDto fileResultDto = new BlogResultDto();
        List<UserHistoryHome> userHistoryHomes = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            userHistoryHomes = userHistoryHomeRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistoryHome> userHistoryHomeList = userHistoryHomes;
        BlogResults.stream().parallel().forEach(x -> {
            setLikeAndCard(userHistoryHomeList, x);
        });
        fileResultDto.setList(BlogResults.getContent());
        PaginationModel paginationModel = new PaginationModel(BlogResults.getPageable().getPageNumber(), BlogResults.getPageable().getPageSize(), (int) BlogResults.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;

    }

    private void setLikeAndCard(List<UserHistoryHome> finalFileHistoryHome, BlogResult x) {
        if (!finalFileHistoryHome.isEmpty()) {
            List<UserHistoryHome> collect = finalFileHistoryHome.stream().filter(f -> f.getHomeId().toString().equals(x.getId().toString())).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(LIKE_BLOG)));
            }
        }

    }


    public TotalMyDTO myFileTotal() {

        StringBuilder isLikeSql = new StringBuilder();
        StringBuilder byUserSql = new StringBuilder();
        Integer loginId = userService.getUserLogin().getId();
        isLikeSql.append(sqlHome(loginId, LIKE_BLOG));
        byUserSql.append(sqlHome(loginId, UPLOAD_BLOG));


        Integer isLike = ((Number) entityManager.createNativeQuery(" select count(j.id) " + isLikeSql).getSingleResult()).intValue();
        ;
        Integer byUser = ((Number) entityManager.createNativeQuery(" select count(j.id) " + byUserSql).getSingleResult()).intValue();
        ;
        TotalMyDTO totalMyDTO = new TotalMyDTO();
        totalMyDTO.setIsUser(byUser);
        totalMyDTO.setIsLike(isLike);
        return totalMyDTO;


    }


}
