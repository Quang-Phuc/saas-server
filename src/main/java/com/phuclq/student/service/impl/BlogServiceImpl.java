package com.phuclq.student.service.impl;

import com.phuclq.student.common.Constants;
import com.phuclq.student.dao.BlogDao;
import com.phuclq.student.domain.*;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.blog.*;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.exception.NotFoundException;
import com.phuclq.student.repository.*;
import com.phuclq.student.security.AuthoritiesConstants;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.BlogService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.ActivityConstants;
import com.phuclq.student.types.NotificationType;
import com.phuclq.student.types.StatusType;
import com.phuclq.student.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.phuclq.student.types.ActivityConstants.UPLOAD_BLOG;
import static com.phuclq.student.types.FileType.FILE_BLOG;
import static com.phuclq.student.utils.HtmlIdInserter.addUniqueIdsToHeadersIfAbsent;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    CategoryBlogRepository categoryBlogRepository;

    @Autowired
    BlogRepository blogRepository;
    @Autowired
    UserService userService;
    @Autowired
    BlogDao homeDao;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    private AttachmentService attachmentService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private FCMService fcmService;
    @Autowired
    private UserHistoryBlogRepository userHistoryBlogRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public Long create(BlogRequest dto) throws IOException {
        Integer login = userService.getUserLogin().getId();
        Blog blog = new Blog();
        if (Objects.nonNull(dto.getId())) {

            blog = blogRepository.findById(dto.getId()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
            if (Objects.nonNull(dto.getTitle())) {
                blog.setIdUrl(getSearchableStringUrl(dto.getTitle(), blogRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getTitle())).size()));
            }
        } else {
            blog.setIdUrl(getSearchableStringUrl(dto.getTitle(), blogRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getTitle())).size()));

        }
        blog.setIsDeleted(false);

        if (Objects.nonNull(dto.getTitle())) {
            blog.setTitle(dto.getTitle());
        }
        if (Objects.nonNull(dto.getAlt())) {
            blog.setAlt(dto.getAlt());
        }
        if (Objects.nonNull(dto.getCategoryBlogId())) {
            blog.setCategoryBlogId(dto.getCategoryBlogId());
        }
        if (Objects.nonNull(dto.getContent())) {
            blog.setContent(addUniqueIdsToHeadersIfAbsent(dto.getContent()));
        }
        if (Objects.nonNull(dto.getDescription())) {
            blog.setDescription(dto.getDescription());
        }
        if (Objects.nonNull(dto.getIdUrl())) {
            blog.setIdUrl(dto.getIdUrl());
        }
        blog.setApproverId(userService.getUserLogin().getId());
        blog.setApprovedDate(DateTimeUtils.timeNow());
        blog.setCategoryBlogName(categoryBlogRepository.findById(blog.getCategoryBlogId()).get().getName());
        Blog result = blogRepository.save(blog);
        if (Objects.isNull(dto.getId())) {
            activityHome(result.getId(), UPLOAD_BLOG);
        }
        if (Objects.nonNull(dto.getFiles())) {

            List<Attachment> listAttachmentsFromBase64S3 = attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), result.getId().intValue(), login, false);

            result.setUrl(listAttachmentsFromBase64S3.stream().findFirst().orElseThrow(NotFoundException::new).getUrl());
            blogRepository.save(result);
        }

        return result.getId();
    }

    @Override
    public UserHistoryBlog activityHome(Long id, Integer activity) {

        UserHistoryBlog userHistoryHome = new UserHistoryBlog();
        userHistoryHome.setActivityId(activity);
        userHistoryHome.setBlogId(id);
        return userHistoryBlogRepository.save(userHistoryHome);
    }

    @Override
    public BlogResultDto searchBlog(Pageable pageable, BlogRequest request) {
        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(Constants.SQL_BLOG_JOIN);
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
            sqlStatement.append(" or LOWER(j.description) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.full_name) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
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
            list.add(result);
        }

        Page<BlogResult> homeResults = new PageImpl<BlogResult>(list, pageable, count);
        BlogResultDto fileResultDto = new BlogResultDto();
        List<UserHistoryBlog> userHistoryBlog = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            userHistoryBlog = userHistoryBlogRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistoryBlog> userHistoryHomeList = userHistoryBlog;
        homeResults.stream().parallel().forEach(x -> {
            setLikeAndCard(userHistoryHomeList, x);
        });
        fileResultDto.setList(homeResults.getContent());
        PaginationModel paginationModel = new PaginationModel(homeResults.getPageable().getPageNumber(), homeResults.getPageable().getPageSize(), (int) homeResults.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    private void setLikeAndCard(List<UserHistoryBlog> finalFileHistoryHome, BlogResult x) {
        if (!finalFileHistoryHome.isEmpty()) {
            List<UserHistoryBlog> collect = finalFileHistoryHome.stream().filter(f -> f.getBlogId().toString().equals(x.getId().toString())).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(ActivityConstants.LIKE_BLOG)));
            }
        }

    }

    @Override
    public Long createCategoryBlog(CategoryBLog dto) {
        CategoryBLog save = Objects.nonNull(dto.getId()) ? categoryBlogRepository.findAllById(dto.getId()) : dto;
        if (Objects.nonNull(dto.getId())) {
            if (Objects.nonNull(dto.getName())) {
                save.setIdUrl(getSearchableStringUrl(dto.getName(), categoryBlogRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));
            }
        } else {
            if(Objects.nonNull(categoryBlogRepository.findAllByName(dto.getName()))){
                throw new BusinessHandleException("SS024");
            }
            save.setIdUrl(getSearchableStringUrl(dto.getName(), categoryBlogRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));

        }
        if(Objects.nonNull(dto.getIdUrl())){
            save.setIdUrl(getSearchableStringUrl(dto.getIdUrl(), categoryBlogRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getName())).size()));

        }
        if (Objects.nonNull(dto.getName())) {
            save.setName(dto.getName());
        }
        categoryBlogRepository.save(save);
        return save.getId();
    }

    @Override
    public List<CategoryBLog> searchCategoryBlog() {
        return categoryBlogRepository.findAll();
    }

    @Override
    public Page<CategoryBLog> findAll(Pageable pageable, String search) {
        return Objects.nonNull(search) ? categoryBlogRepository.findAllByNameContainingIgnoreCase(search.trim(), pageable) : categoryBlogRepository.findAll(pageable);
    }

    @Override
    public BlogDetailDto findAllBlogById(String idUrlCategory, String idUrl) {
        BlogDetailDto blogDetailDto = new BlogDetailDto();
        blogDetailDto.setBlog(blogRepository.findAllByIdUrl(idUrl).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST)));
        List<BlogDto> allByCategoryBlogId = blogRepository.findByCategory(blogDetailDto.getBlog().getCategoryBlogId());

        Collections.shuffle(allByCategoryBlogId);
        blogDetailDto.setBlogSeeMore(allByCategoryBlogId.subList(0, Math.min(5, allByCategoryBlogId.size())));
        return blogDetailDto;
    }


    @Override
    public void deleteBlogById(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public CategoryBLog findAllCategoryBlogById(Long id) {
        return categoryBlogRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
    }

    @Override
    public void deleteCategoryBlogById(Long id) {
        categoryBlogRepository.deleteById(id);
    }

    @Override
    public void approve(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(NotFoundException::new);
        blog.setApproverId(userService.getUserLogin().getId());
        blog.setApprovedDate(DateTimeUtils.timeNow());
        blogRepository.save(blog);
        fcmService.tokenFireBase(NotificationType.BLOG_ACCEPTED.getTitle(), NotificationType.BLOG_ACCEPTED.getMessage(), new Notification(NotificationType.BLOG_ACCEPTED.getCode(), blog.getCreatedBy(), NotificationType.BLOG_ACCEPTED.getType(), StatusType.DONE.getName(), NotificationType.HOME_ACCEPTED.getMessage(), NotificationType.BLOG_ACCEPTED.getImageIcon(), NotificationType.BLOG_ACCEPTED.getUrlDetail()));

    }

    @Override
    public BlogResultDto myHome(BlogRequest request, Pageable pageable) {
        return homeDao.myHome(request, pageable);
    }

    @Override
    public TotalMyDTO total() {
        return homeDao.myFileTotal();
    }

    @Override
    public void deleteActivityHome(Long id, Integer card) {
        UserHistoryBlog allByCreatedByAndActivityIdAndHomeId = userHistoryBlogRepository.findAllByCreatedByAndActivityIdAndBlogId(userService.getUserLogin().getId().toString(), card, id);
        if (Objects.isNull(allByCreatedByAndActivityIdAndHomeId)) {
            throw new BusinessHandleException("SS000");
        }
        userHistoryBlogRepository.delete(allByCreatedByAndActivityIdAndHomeId);
    }

    @Override
    public Page<HistoryFileResult> deleteHistory(BlogRequest request) {
        User user = userService.getUserLogin();
        List<UserHistoryBlog> userHistoryBlogList = new ArrayList<>();
        if (userRoleRepository.findByRole(AuthoritiesConstants.ADMIN).get().getId().equals(user.getRoleId())) {
            userHistoryBlogList = userHistoryBlogRepository.findAllByActivityIdAndBlogIdIn(request.getActivityId(), request.getIds());
        } else {
            userHistoryBlogList = userHistoryBlogRepository.findAllByActivityIdAndBlogIdInAndCreatedBy(request.getActivityId(), request.getIds(), user.getId().toString());
        }

        userHistoryBlogRepository.deleteAll(userHistoryBlogList);
        List<Blog> allByIdIn = blogRepository.findAllByIdIn(request.getIds());
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
        blogRepository.saveAll(allByIdIn);


        return null;
    }


}

