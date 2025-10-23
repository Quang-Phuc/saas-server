package com.phuclq.student.service.impl;

import com.phuclq.student.controller.FileHistoryController.HistoryFileRequest;
import com.phuclq.student.dao.FileDao;
import com.phuclq.student.domain.File;
import com.phuclq.student.domain.User;
import com.phuclq.student.domain.UserHistory;
import com.phuclq.student.domain.UserHistoryFile;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.FileResultDto;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.repository.*;
import com.phuclq.student.security.AuthoritiesConstants;
import com.phuclq.student.service.HistoryFileService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.utils.DateTimeUtils;
import com.phuclq.student.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.phuclq.student.types.ActivityConstants.UPLOAD;
import static com.phuclq.student.types.FileType.*;

@Service
public class HistoryFileServiceImpl implements HistoryFileService {

    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    FileDao fileDao;
    @Autowired
    private FileRepository fileRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserHistoryFileRepository userHistoryFileRepository;
    @Autowired
    private UserHistoryRepository userHistoryRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public FileResultDto getFile(FileHomePageRequest request, Pageable pageable) {

        return fileDao.myFile(request, pageable);


    }

    @Override
    public TotalMyDTO total() {
        return fileDao.myFileTotal();
    }

    @Override
    public Page<HistoryFileResult> getFileDownload(HistoryFileRequest request, Pageable pageable) {
        User user = userService.getUserLogin();
        Timestamp dateFrom = null;
        Timestamp dateTo = null;
        if (StringUtils.isStringNotNullAndHasValue(request.getDateFrom())) {
            dateFrom = DateTimeUtils.convertDateToTimestamp(DateTimeUtils.toDateFromStr(request.getDateFrom(), DateTimeUtils.yyyy_MM_dd));
            dateTo = !StringUtils.isStringNotNullAndHasValue(request.getDateTo()) ? DateTimeUtils.convertDateToTimestamp(DateTimeUtils.toDateFromStr("9999-12-31", DateTimeUtils.yyyy_MM_dd)) : DateTimeUtils.convertDateToTimestamp(DateTimeUtils.toDateFromStr(request.getDateTo(), DateTimeUtils.yyyy_MM_dd));
        }
        if (!StringUtils.isStringNotNullAndHasValue(request.getDateFrom()) && StringUtils.isStringNotNullAndHasValue(request.getDateTo())) {
            dateFrom = DateTimeUtils.toTimestampFromStr("1900-01-01", DateTimeUtils.yyyy_MM_dd);
            dateTo = DateTimeUtils.toTimestampFromStr(request.getDateTo(), DateTimeUtils.yyyy_MM_dd);
        }

        Page<HistoryFileResult> page = null;

        if (StringUtils.isStringNotNullAndHasValue(request.getDateFrom()) || StringUtils.isStringNotNullAndHasValue(request.getDateTo())) {
            page = fileRepo.getFileByUserDownloaded(dateFrom, dateTo, request.getTitle(), user.getId(), pageable);
        } else {
            page = fileRepo.getFileByUserDownloaded(request.getTitle(), user.getId(), pageable);
        }
        return page;
    }


    @Override
    public Page<HistoryFileResult> getFileFavoriteByDate(String dateFromStr, String dateToStr, Pageable pageable) {
        User user = userService.getUserLogin();
        Timestamp dateFrom = null;
        Timestamp dateTo = null;
        if (!dateFromStr.isEmpty()) {
            dateFrom = DateTimeUtils.toTimestampFromStr(dateFromStr, DateTimeUtils.DATE_TIME_MYSQL_FORMAT);
        }
        if (!dateToStr.isEmpty()) {
            dateTo = DateTimeUtils.toTimestampFromStr(dateToStr, DateTimeUtils.DATE_TIME_MYSQL_FORMAT);
        }
        if (dateFrom != null && dateTo != null) {
            return fileRepo.getFileUserFavoriteByDate(dateFrom, dateTo, user.getId(), pageable);
        } else {
            return fileRepo.getFileByUserFavorite(user.getId(), pageable);
        }
    }

    @Override
    public Page<HistoryFileResult> deleteFileHistory(FileHomePageRequest request) {
        User user = userService.getUserLogin();
        List<UserHistoryFile> fileHistoryByUser = new ArrayList<>();
        if (userRoleRepository.findByRole(AuthoritiesConstants.ADMIN).get().getId().equals(user.getRoleId())) {
            fileHistoryByUser = userHistoryFileRepository.findFileHistory(request.getActivityId(), request.getFileIds());
        } else {
            fileHistoryByUser = userHistoryFileRepository.findFileHistoryByUser(request.getActivityId(), request.getFileIds(), user.getId());
        }

        fileHistoryByUser.forEach(x -> {
            {
                Optional<UserHistory> history = userHistoryRepository.findById(x.getUserHisotyId());
                userHistoryFileRepository.delete(x);
                userHistoryRepository.delete(history.get());
            }
        });
        request.getFileIds().forEach(y -> {
            if (UPLOAD.equals(request.getActivityId())) {
                File fileOptional = fileRepo.findById(y).get();
                fileOptional.setIsDeleted(true);
                fileOptional.setDeleteId(user.getId());
                Instant instant = Instant.now();
                Timestamp timestamp = Timestamp.from(instant);
                fileOptional.setDeleteDate(timestamp);
                List<String> typeFiles = Arrays.asList(FILE_AVATAR.getName(), FILE_ZIP.getName(), FILE_CONVERT_DOC_PDF.getName(), FILE_CUT.getName(), FILE_DEMO.getName(), FILE_UPLOAD.getName());
                attachmentRepository.deleteAll(attachmentRepository.findAllByRequestIdAndFileTypeIn(y, typeFiles));
                fileRepo.save(fileOptional);
            }
        });


        return null;
    }
}
