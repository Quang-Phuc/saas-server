package com.phuclq.student.service.impl;

import com.phuclq.student.domain.UserHistory;
import com.phuclq.student.domain.UserHistoryFile;
import com.phuclq.student.repository.ActivityRepository;
import com.phuclq.student.repository.CategoryRepository;
import com.phuclq.student.repository.UserHistoryFileRepository;
import com.phuclq.student.repository.UserHistoryRepository;
import com.phuclq.student.service.ActivityService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserHistoryRepository userHistoryRepository;
    @Autowired
    private UserHistoryFileRepository userHistoryFileRepository;


    @Override
    public void updateLikeFile(Integer idFile) {
        Integer idLogin = userService.getUserLogin().getId();
        UserHistory userHistory = new UserHistory();
        userHistory.setActivityId(3);
        userHistory.setUserId(idLogin);
        UserHistory userHistorySave = userHistoryRepository.save(userHistory);

        UserHistoryFile userHistoryFile = new UserHistoryFile();

        Instant instant = Instant.now();
        Timestamp timestamp = Timestamp.from(instant);
        userHistoryFile.setActivityDate(timestamp);
        userHistoryFile.setFileId(idFile);
        userHistoryFile.setUserHisotyId(userHistorySave.getId());
        userHistoryFileRepository.save(userHistoryFile);


    }
}

