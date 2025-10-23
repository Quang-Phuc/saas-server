package com.phuclq.student.service.impl;

import com.phuclq.student.common.Constants;
import com.phuclq.student.domain.CV;
import com.phuclq.student.domain.Job;
import com.phuclq.student.domain.Notification;
import com.phuclq.student.domain.UserHistoryJob;
import com.phuclq.student.dto.*;
import com.phuclq.student.dto.job.HomeResultDto;
import com.phuclq.student.dto.job.JobRequest;
import com.phuclq.student.dto.webhook.HomeDto;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.exception.NotFoundException;
import com.phuclq.student.repository.CVRepository;
import com.phuclq.student.repository.DistrictRepository;
import com.phuclq.student.repository.JobRepository;
import com.phuclq.student.repository.UserHistoryJobRepository;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.JobService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.LevelJobType;
import com.phuclq.student.types.NotificationType;
import com.phuclq.student.types.StatusType;
import com.phuclq.student.types.UserHistoryJobTypeConstants;
import com.phuclq.student.utils.DateTimeUtils;
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
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.phuclq.student.types.ActivityConstants.*;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrl;
import static com.phuclq.student.utils.StringUtils.getSearchableStringUrlExit;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private CVRepository cvRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserHistoryJobRepository userHistoryJobRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${file.url.home.find}")
    private String fileUrlHomeFind;

    @Autowired
    private FCMService fcmService;

    @Override
    public Long createOrUpdateCV(JobRequest dto) throws IOException {
        Integer login = userService.getUserLogin().getId();
        CV cv = new CV();
        if (Objects.nonNull(dto.getId())) {

            cv = cvRepository.findById(dto.getId()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
        }
        BeanUtils.copyProperties(dto, cv);
        cv.setApproverId(null);
        cv.setApprovedDate(null);
        CV result = cvRepository.save(cv);
        if (Objects.nonNull(dto.getFiles())) {

            attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), result.getId().intValue(), login, false);
        }
        if (Objects.isNull(dto.getId())) {
            activityJobAndCV(result.getId(), UserHistoryJobTypeConstants.CV, true);
        }
        return result.getId();

    }

    @Override
    public Map level() {
        return LevelJobType.level;

    }

    @Override
    public JobResultDto search(JobRequest request, Pageable pageable, Boolean user) {

        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(Constants.SQL_JOB_JOIN);

        if (user) {
            sqlStatement.append(" and j.approver_id is not null ");
        }
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(0)) {
            sqlStatement.append(" and j.approver_id is  null ");
        }
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(1)) {
            sqlStatement.append(" and j.approver_id is not  null ");
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
        if (Objects.nonNull(request.getJobType())) {
            sqlStatement.append(" and j.job_type = ? ");
            listParam.add(request.getJobType());
        }
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(j.company_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.title) like LOWER(?)) ");
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
        Query query = entityManager.createNativeQuery(Constants.SQL_JOB + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<JobResult> list = new ArrayList<>();
        for (Object obj : objList) {
            JobResult result = new JobResult((Object[]) obj);
            list.add(result);
        }

        Page<JobResult> file = new PageImpl<JobResult>(list, pageable, count);
        JobResultDto fileResultDto = new JobResultDto();
        List<UserHistoryJob> fileHistoryHome = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            fileHistoryHome = userHistoryJobRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistoryJob> finalFileHistoryHome = fileHistoryHome;
        file.stream().parallel().forEach(x -> {
            setLikeAndCard(finalFileHistoryHome, x);
        });
        fileResultDto.setList(file.getContent());
        PaginationModel paginationModel = new PaginationModel(file.getPageable().getPageNumber(),
                file.getPageable().getPageSize(), (int) file.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    @Override
    public JobCVResultDto searchCV(JobRequest request, Pageable pageable, Boolean user) {
        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(Constants.SQL_JOB_CV_JOIN);

        if (user) {
            sqlStatement.append(" and j.approver_id is not null ");
        }
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(0)) {
            sqlStatement.append(" and j.approver_id is  null ");
        }
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(1)) {
            sqlStatement.append(" and j.approver_id is not  null ");
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
        if (Objects.nonNull(request.getJobType())) {
            sqlStatement.append(" and j.job_type = ? ");
            listParam.add(request.getJobType());
        }
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(j.address) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.title) like LOWER(?)) ");
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
        Query query = entityManager.createNativeQuery(Constants.SQL_JOB_CV + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<JobCVResult> list = new ArrayList<>();
        for (Object obj : objList) {
            JobCVResult result = new JobCVResult((Object[]) obj);
            list.add(result);
        }

        Page<JobCVResult> file = new PageImpl<JobCVResult>(list, pageable, count);
        JobCVResultDto fileResultDto = new JobCVResultDto();
        List<UserHistoryJob> fileHistoryHome = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            fileHistoryHome = userHistoryJobRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistoryJob> finalFileHistoryHome = fileHistoryHome;
        file.stream().parallel().forEach(x -> {
            setLikeAndCard(finalFileHistoryHome, x);
        });
        fileResultDto.setList(file.getContent());
        PaginationModel paginationModel = new PaginationModel(file.getPageable().getPageNumber(),
                file.getPageable().getPageSize(), (int) file.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    private void setLikeAndCard(List<UserHistoryJob> finalFileHistoryHome, JobResult x) {
        if (finalFileHistoryHome.size() > 0) {
            List<UserHistoryJob> collect = finalFileHistoryHome.stream()
                    .filter(f -> f.getJobId().equals(x.getId())).collect(Collectors.toList());
            if (collect.size() > 0) {
                x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(LIKE_JOB)));
            }
        }
    }


    private void setLikeAndCard(List<UserHistoryJob> finalFileHistoryHome, JobCVResult x) {
        if (finalFileHistoryHome.size() > 0) {
            List<UserHistoryJob> collect = finalFileHistoryHome.stream()
                    .filter(f -> f.getJobId().equals(x.getId())).collect(Collectors.toList());
            if (collect.size() > 0) {
                x.setIsLike(collect.stream().anyMatch(f -> f.getActivityId().equals(LIKE_JOB_CV)));
            }
        }
    }

    @Override
    public Long creatOrUpdateJob(JobRequest dto) throws IOException {
        Integer login = userService.getUserLogin().getId();
        Job job = new Job();
        if (Objects.nonNull(dto.getId())) {
            job.setIdUrl(getSearchableStringUrl(dto.getJobName(), jobRepository.findByIdUrlStartingWith(getSearchableStringUrlExit(dto.getJobName())).size()));

            job = jobRepository.findById(dto.getId()).orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
        }
        BeanUtils.copyProperties(dto, job);
        job.setApproverId(1);
        job.setApprovedDate(DateTimeUtils.timeNow());
        Job result = jobRepository.save(job);
        if (Objects.nonNull(dto.getFiles())) {

            attachmentService.createListAttachmentsFromBase64S3(dto.getFiles(), result.getId().intValue(), login, false);
        }
        if (Objects.isNull(dto.getId())) {
            activityJobAndCV(result.getId(), UserHistoryJobTypeConstants.JOB, true);
        }
        return result.getId();
    }

    @Override
    public HomeResultDto topSame(JobRequest jobRequest, Pageable pageable) {
        return null;
    }

    @Override
    public void approveJob(Long id, String type) {
        if (UserHistoryJobTypeConstants.CV.equalsIgnoreCase(type.trim())) {
            CV job = cvRepository.findById(id).orElseThrow(NotFoundException::new);
            job.setApproverId(userService.getUserLogin().getId());
            job.setApprovedDate(DateTimeUtils.timeNow());
            cvRepository.save(job);
            fcmService.tokenFireBase(NotificationType.CV_ACCEPTED.getTitle(), NotificationType.CV_ACCEPTED.getMessage(), new Notification(NotificationType.CV_ACCEPTED.getCode(), job.getCreatedBy(), NotificationType.CV_ACCEPTED.getType(), StatusType.DONE.getName(), NotificationType.CV_ACCEPTED.getMessage(), NotificationType.CV_ACCEPTED.getImageIcon(), NotificationType.CV_ACCEPTED.getUrlDetail()));

        } else {
            Job job = jobRepository.findById(id).orElseThrow(NotFoundException::new);
            job.setApproverId(userService.getUserLogin().getId());
            job.setApprovedDate(DateTimeUtils.timeNow());
            jobRepository.save(job);
            fcmService.tokenFireBase(NotificationType.JOB_ACCEPTED.getTitle(), NotificationType.JOB_ACCEPTED.getMessage(), new Notification(NotificationType.JOB_ACCEPTED.getCode(), job.getCreatedBy(), NotificationType.JOB_ACCEPTED.getType(), StatusType.DONE.getName(), NotificationType.JOB_ACCEPTED.getMessage(), NotificationType.HOME_ACCEPTED.getImageIcon(), NotificationType.JOB_ACCEPTED.getUrlDetail()));
        }
    }

    @Override
    public HomeDto findAllById(Long id) {
        return null;
    }

    @Override
    public void deleteById(String type, Long id) {
        if (UserHistoryJobTypeConstants.CV.equalsIgnoreCase(type.trim())) {
            CV allByIdAndCreatedBy = cvRepository.findAllByIdAndCreatedBy(id, userService.getUserLogin().getId().toString());
            cvRepository.delete(allByIdAndCreatedBy);

        } else {
            Job allByIdAndCreatedBy = jobRepository.findAllByIdAndCreatedBy(id, userService.getUserLogin().getId().toString());
            jobRepository.delete(allByIdAndCreatedBy);
        }
    }

    @Override
    public void adminDeleteById(String type, Long id) {
        if (UserHistoryJobTypeConstants.CV.equalsIgnoreCase(type.trim())) {
            cvRepository.deleteById(id);
        } else {
            jobRepository.deleteById(id);
        }
    }

    @Override
    public UserHistoryJob activityJobAndCV(Long id, String type, Boolean activityUpload) {
        Integer activityId = null;
        if (!activityUpload) {
            activityId = UserHistoryJobTypeConstants.CV.equalsIgnoreCase(type.trim()) ? LIKE_JOB_CV : LIKE_JOB;
        } else {
            activityId = UserHistoryJobTypeConstants.CV.equalsIgnoreCase(type.trim()) ? UPLOAD_JOB_CV : UPLOAD_JOB;
        }
        UserHistoryJob userHistoryJob = new UserHistoryJob();
        userHistoryJob.setActivityId(activityId);
        userHistoryJob.setJobId(id);
        userHistoryJob.setType(type);
        return userHistoryJobRepository.save(userHistoryJob);
    }

    @Override
    public void deleteActivityJob(Long id, String type) {
        Integer activityId = UserHistoryJobTypeConstants.CV.equalsIgnoreCase(type.trim()) ? LIKE_JOB_CV : LIKE_JOB;
        UserHistoryJob allByCreatedByAndActivityIdAndHomeId = userHistoryJobRepository.findAllByCreatedByAndActivityIdAndJobIdAndType(userService.getUserLogin().getId().toString(), activityId, id, type);
        if (Objects.isNull(allByCreatedByAndActivityIdAndHomeId)) {
            throw new BusinessHandleException("SS000");
        }
        userHistoryJobRepository.delete(allByCreatedByAndActivityIdAndHomeId);

    }

    public String sqlJobCV(Integer loginId, Integer activity) {
        return "from cv j " +
                "         join province p on j.province_id = p.id\n" +
                "         join district d on j.district_id = d.id\n" +
                "         join ward w on j.ward_id = w.id\n" +
                "         join user_history_job uhh on j.id = uhh.JOB_ID and uhh.created_by = " + loginId + " and uhh.type = 'CV' " +
                "         join attachment  a on j.id = a.request_id and a.type ='JOB_CV_AVATAR'  where j.is_deleted =0  and uhh.activity_id = " + activity + "  ";
    }

    public String sqlJob(Integer loginId, Integer activity) {
        return "from job j " +
                "         join province p on j.province_id = p.id\n" +
                "         join district d on j.district_id = d.id\n" +
                "         join ward w on j.ward_id = w.id\n" +
                "         join user_history_job uhh on j.id = uhh.JOB_ID and uhh.created_by = " + loginId + " and uhh.type = 'CV' " +
                "         join attachment  a on j.id = a.request_id and a.type ='JOB_AVATAR'  where j.is_deleted =0  and uhh.activity_id = " + activity + "  ";
    }

    @Override
    public JobCVResultDto sqlJobCV(JobRequest request, Pageable pageable) {
        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();
        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(sqlJobCV(loginId, request.getActivityId())
        );
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(0)) {
            sqlStatement.append(" and j.approver_id is  null ");
        }
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(1)) {
            sqlStatement.append(" and j.approver_id is not  null ");
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

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(j.address) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.title) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
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
        Query query = entityManager.createNativeQuery(Constants.SQL_JOB_CV + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<JobCVResult> list = new ArrayList<>();
        for (Object obj : objList) {
            JobCVResult result = new JobCVResult((Object[]) obj);
            if (Objects.isNull(result.getUrl())) {
                result.setUrl(fileUrlHomeFind);
            }
            list.add(result);
        }

        Page<JobCVResult> homeResults = new PageImpl<JobCVResult>(list, pageable, count);
        JobCVResultDto fileResultDto = new JobCVResultDto();
        List<UserHistoryJob> userHistoryHomes = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            userHistoryHomes = userHistoryJobRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistoryJob> userHistoryHomeList = userHistoryHomes;
        homeResults.stream().parallel().forEach(x -> {
            setLikeAndCard(userHistoryHomeList, x);
        });
        fileResultDto.setList(homeResults.getContent());
        PaginationModel paginationModel = new PaginationModel(homeResults.getPageable().getPageNumber(), homeResults.getPageable().getPageSize(), (int) homeResults.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    @Override
    public TotalMyDTO total(String type) {
        StringBuilder isLikeSql = new StringBuilder();
        StringBuilder byUserSql = new StringBuilder();
        Integer loginId = userService.getUserLogin().getId();
        isLikeSql.append(sqlJob(loginId, LIKE_JOB));
        byUserSql.append(sqlJob(loginId, UPLOAD_JOB));


        Integer isLike = ((Number) entityManager.createNativeQuery(" select count(j.id) " + isLikeSql).getSingleResult()).intValue();
        ;
        Integer byUser = ((Number) entityManager.createNativeQuery(" select count(j.id) " + byUserSql).getSingleResult()).intValue();
        ;
        TotalMyDTO totalMyDTO = new TotalMyDTO();
        totalMyDTO.setIsUser(byUser);
        totalMyDTO.setIsLike(isLike);
        return totalMyDTO;
    }

    @Override
    public Page<HistoryFileResult> deleteHistory(JobRequest jobRequest) {
        return null;
    }

    @Override
    public JobResultDto searchAdmin(JobRequest request, Pageable pageable, Boolean user) {
        return search(request, pageable, false);
    }

    @Override
    public JobCVResultDto searchAdminCV(JobRequest request, Pageable pageable) {
        return searchCV(request, pageable, false);
    }

    @Override
    public JobCVResultDto sqlJob(JobRequest request, Pageable pageable) {
        List<Object> objList = null;
        Integer loginId = userService.getUserLogin().getId();
        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(sqlJob(loginId, request.getActivityId())
        );
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(0)) {
            sqlStatement.append(" and j.approver_id is  null ");
        }
        if (Objects.nonNull(request.getApprove()) && request.getApprove().equals(1)) {
            sqlStatement.append(" and j.approver_id is not  null ");
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
        if (Objects.nonNull(request.getJobType())) {
            sqlStatement.append(" and j.job_type = ? ");
            listParam.add(request.getJobType());
        }
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(j.company_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(j.title) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
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
        Query query = entityManager.createNativeQuery(Constants.SQL_JOB + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<JobCVResult> list = new ArrayList<>();
        for (Object obj : objList) {
            JobCVResult result = new JobCVResult((Object[]) obj);
            if (Objects.isNull(result.getUrl())) {
                result.setUrl(fileUrlHomeFind);
            }
            list.add(result);
        }

        Page<JobCVResult> homeResults = new PageImpl<JobCVResult>(list, pageable, count);
        JobCVResultDto fileResultDto = new JobCVResultDto();
        List<UserHistoryJob> userHistoryHomes = new ArrayList<>();
        if (Objects.nonNull(loginId)) {
            userHistoryHomes = userHistoryJobRepository.findAllByCreatedBy(loginId.toString());

        }
        List<UserHistoryJob> userHistoryHomeList = userHistoryHomes;
        homeResults.stream().parallel().forEach(x -> {
            setLikeAndCard(userHistoryHomeList, x);
        });
        fileResultDto.setList(homeResults.getContent());
        PaginationModel paginationModel = new PaginationModel(homeResults.getPageable().getPageNumber(), homeResults.getPageable().getPageSize(), (int) homeResults.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    @Override
    public TotalMyDTO totalCV() {
        StringBuilder isLikeSql = new StringBuilder();
        StringBuilder byUserSql = new StringBuilder();
        Integer loginId = userService.getUserLogin().getId();
        isLikeSql.append(sqlJobCV(loginId, LIKE_JOB_CV));
        byUserSql.append(sqlJobCV(loginId, UPLOAD_JOB_CV));


        Integer isLike = ((Number) entityManager.createNativeQuery(" select count(j.id) " + isLikeSql).getSingleResult()).intValue();
        ;
        Integer byUser = ((Number) entityManager.createNativeQuery(" select count(j.id) " + byUserSql).getSingleResult()).intValue();
        ;
        TotalMyDTO totalMyDTO = new TotalMyDTO();
        totalMyDTO.setIsUser(byUser);
        totalMyDTO.setIsLike(isLike);
        return totalMyDTO;
    }

    @Transactional()
    @Override
    public CountResponse getCount(String type) {
        String sql;
        if (type.equalsIgnoreCase("CV")) {
            sql = "select count(j.id) as total, sum(case when j.approver_id is not null then 1 else 0 end) as approved, " +
                    "sum(case when j.approver_id is null then 1 else 0 end) as unapproved " +
                    "from cv j " +
                    "join province p on j.province_id = p.id " +
                    "join district d on j.district_id = d.id " +
                    "join ward w on j.ward_id = w.id " +
                    "join attachment a on j.id = a.request_id and a.type ='JOB_CV_AVATAR' " +
                    "where j.is_deleted = 0";
        } else if (type.equalsIgnoreCase("JOB")) {
            sql = "select count(j.id) as total, sum(case when j.approver_id is not null then 1 else 0 end) as approved, " +
                    "sum(case when j.approver_id is null then 1 else 0 end) as unapproved " +
                    "from job j " +
                    "join province p on j.province_id = p.id " +
                    "join district d on j.district_id = d.id " +
                    "join ward w on j.ward_id = w.id " +
                    "join attachment a on j.id = a.request_id and a.type ='JOB_AVATAR' " +
                    "where j.is_deleted = 0";
        } else {
            return null; // or throw exception for invalid input
        }

        Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();

        int total = ((Number) result[0]).intValue();
        int approved = result[1] != null ? ((Number) result[1]).intValue() : 0;
        int unapproved = result[2] != null ? ((Number) result[2]).intValue() : 0;

        return new CountResponse(total, approved, unapproved);
    }
}
