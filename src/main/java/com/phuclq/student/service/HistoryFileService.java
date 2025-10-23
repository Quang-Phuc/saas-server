package com.phuclq.student.service;

import com.phuclq.student.controller.FileHistoryController.HistoryFileRequest;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.FileResultDto;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.TotalMyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryFileService {
    FileResultDto getFile(FileHomePageRequest request, Pageable pageable);

    TotalMyDTO total();

    Page<HistoryFileResult> getFileDownload(HistoryFileRequest request, Pageable pageable);

    Page<HistoryFileResult> getFileFavoriteByDate(String dateFrom, String dateTo, Pageable pageable);

    Page<HistoryFileResult> deleteFileHistory(FileHomePageRequest request);
}
