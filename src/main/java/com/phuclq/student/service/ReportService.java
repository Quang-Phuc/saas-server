package com.phuclq.student.service;

import com.phuclq.student.domain.Report;
import com.phuclq.student.dto.ReportDTO;
import com.phuclq.student.dto.ReportResult;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    Report createReport(Report reportDTO);


    ReportResult search(ReportDTO reportDTO, Pageable pageable);

    void deleteById(Long id);

    Report findAllById(Long id);
}
