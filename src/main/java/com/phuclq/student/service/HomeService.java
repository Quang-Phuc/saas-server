package com.phuclq.student.service;

import com.phuclq.student.domain.UserHistoryHome;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.home.HomeRequest;
import com.phuclq.student.dto.job.HomeResultDto;
import com.phuclq.student.dto.webhook.HomeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface HomeService {

    Long create(HomeRequest homeRequest) throws IOException;


    HomeResultDto search(HomeRequest request, Pageable pageable);

    HomeResultDto searchAdmin(HomeRequest request, Pageable pageable);

    HomeResultDto topSame(HomeRequest homeRequest, Pageable pageable);

    void approveHome(Long id);

    HomeDto findAllById(String id);

    void deleteById(Long id);

    UserHistoryHome activityHome(Long id, Integer like);

    void deleteActivityHome(Long id, Integer card);

    HomeResultDto myHome(HomeRequest request, Pageable pageable);


    TotalMyDTO total(String type);

    Page<HistoryFileResult> deleteHistory(HomeRequest request);
}
