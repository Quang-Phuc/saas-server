package com.phuclq.student.service.impl;

import com.phuclq.student.common.Constants;
import com.phuclq.student.domain.Report;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.ReportDTO;
import com.phuclq.student.dto.ReportResult;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.repository.ReportRepository;
import com.phuclq.student.service.ReportService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserService userService;

    @Autowired
    private ReportRepository reportRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public ReportResult search(ReportDTO request, Pageable pageable) {
        List objList = null;
        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(" from report r join user u on r.created_by = u.id ");

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER( u.full_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.email) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.user_name) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        if (Objects.nonNull(request.getStartDate())) {
            sqlStatement.append(" and r.created_date >= ? ");
            listParam.add(request.getStartDate());
        }

        if (Objects.nonNull(request.getEndDate())) {
            sqlStatement.append(" and r.created_date <= ? ");
            listParam.add(request.getEndDate());
        }
        if (Objects.nonNull(request.getType())) {

            sqlStatement.append(" and r.type = ? ");
            listParam.add(request.getType());
        }


        Query queryCount = entityManager.createNativeQuery(" select count(r.id) " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(pageable.getPageSize());
        listParam.add(pageable.getPageSize() * pageable.getPageNumber());
        Query query = entityManager.createNativeQuery(Constants.SQL_REPORT + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<ReportDTO> list = new ArrayList<>();
        for (Object obj : objList) {
            ReportDTO result = new ReportDTO((Object[]) obj);
            list.add(result);
        }

        Page<ReportDTO> file = new PageImpl<ReportDTO>(list, pageable, count);
        //add code

        ReportResult fileResultDto = new ReportResult();
        fileResultDto.setList(file.getContent());
        PaginationModel paginationModel = new PaginationModel(file.getPageable().getPageNumber(), file.getPageable().getPageSize(), (int) file.getTotalElements());
        fileResultDto.setPaginationModel(paginationModel);
        return fileResultDto;
    }

    @Override
    public void deleteById(Long id) {
        reportRepository.deleteById(id);
    }

    @Override
    public Report findAllById(Long id) {
        return reportRepository.findById(id).orElseThrow(() -> new BusinessException(
                ExceptionUtils.REQUEST_NOT_EXIST));
    }
}

