package com.phuclq.student.dao;

import com.phuclq.student.common.Constants;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.PaginationModel;
import com.phuclq.student.dto.PaymentResult;
import com.phuclq.student.dto.PaymentResultDto;
import com.phuclq.student.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@Transactional
public class PaymentDao {

    private final Logger log = LoggerFactory.getLogger(PaymentDao.class);

    @Autowired
    private UserService userService;
    @PersistenceContext
    private EntityManager entityManager;


    public PaymentResultDto payment(FileHomePageRequest request, Pageable pageable, Boolean admin) {
        List<Object> objList = null;

        StringBuilder sqlStatement = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(" from payment_request p join user uh  on p.created_by = uh.id where 1 = 1  ");
        if (!admin) {
            sqlStatement.append("  and uh.id = ? ");
            listParam.add(userService.getUserLogin().getId());
        }

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(uh.user_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(uh.email) like LOWER(?) ");
            sqlStatement.append(" or LOWER(uh.full_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(p.account_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(p.account_number) like LOWER(?) ");
            sqlStatement.append(" or LOWER(p.bank_short_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER(uh.phone) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        if (Objects.nonNull(request.getStatus())) {
            sqlStatement.append(" and p.status = ? ");
            listParam.add(request.getStatus());
        }

        if (request.getDateFrom() != null) {
            sqlStatement.append(" and p.CREATED_DATE >= ? ");
            listParam.add(request.getDateFrom());
        }
        if (request.getDateTo() != null) {
            sqlStatement.append(" and p.CREATED_DATE <= ? ");
            listParam.add(request.getDateTo().plusDays(2));
        }

        sqlStatement.append(" order by p.created_date desc ");

        Query queryCount = entityManager.createNativeQuery(" select count(p.id) " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatement.append(" LIMIT ? OFFSET ?");
        listParam.add(pageable.getPageSize());
        listParam.add(pageable.getPageSize() * pageable.getPageNumber());
        Query query = entityManager.createNativeQuery(Constants.SQL_SELECT_PAYMENT + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<PaymentResult> list = new ArrayList<>();
        for (Object obj : objList) {
            PaymentResult result = new PaymentResult((Object[]) obj);
            list.add(result);
        }

        Page<PaymentResult> listCategory = new PageImpl<PaymentResult>(list, pageable, count);
        PaymentResultDto PaymentResultDto = new PaymentResultDto();

        PaymentResultDto.setList(listCategory.getContent());
        PaginationModel paginationModel = new PaginationModel(
                listCategory.getPageable().getPageNumber(), listCategory.getPageable().getPageSize(),
                (int) listCategory.getTotalElements());
        PaymentResultDto.setPaginationModel(paginationModel);
        return PaymentResultDto;

    }


}
