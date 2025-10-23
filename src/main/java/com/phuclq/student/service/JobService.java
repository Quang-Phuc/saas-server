package com.phuclq.student.service;

import com.phuclq.student.domain.UserHistoryJob;
import com.phuclq.student.dto.*;
import com.phuclq.student.dto.job.HomeResultDto;
import com.phuclq.student.dto.job.JobRequest;
import com.phuclq.student.dto.webhook.HomeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Map;

public interface JobService {

    Long createOrUpdateCV(JobRequest jobRequest) throws IOException;


    Long creatOrUpdateJob(JobRequest jobRequest) throws IOException;

    Map level();

    JobResultDto search(JobRequest jobRequest, Pageable pageable, Boolean user);

    JobCVResultDto searchCV(JobRequest jobRequest, Pageable pageable, Boolean user);

    HomeResultDto topSame(JobRequest jobRequest, Pageable pageable);

    void approveJob(Long id, String type);

    HomeDto findAllById(Long id);

    void deleteById(String type, Long id);

    void adminDeleteById(String type, Long id);

    UserHistoryJob activityJobAndCV(Long id, String type, Boolean activityUpload);


    void deleteActivityJob(Long id, String type);

    JobCVResultDto sqlJobCV(JobRequest jobRequest, Pageable pageable);

    JobCVResultDto sqlJob(JobRequest jobRequest, Pageable pageable);


    TotalMyDTO total(String type);

    Page<HistoryFileResult> deleteHistory(JobRequest jobRequest);

    JobResultDto searchAdmin(JobRequest jobRequest, Pageable pageable, Boolean user);

    JobCVResultDto searchAdminCV(JobRequest jobRequest, Pageable pageable);

    TotalMyDTO totalCV();

    CountResponse getCount(String type);
}
