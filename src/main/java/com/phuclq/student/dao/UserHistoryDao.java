package com.phuclq.student.dao;

import com.phuclq.student.common.Constants;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.UserHistoryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static com.phuclq.student.common.Constants.SQL_HISTORY_COIN_JOIN;

@Service
public class UserHistoryDao {

    @PersistenceContext
    private EntityManager entityManager;


    public Page<UserHistoryResult> listUserHistory(FileHomePageRequest request, Pageable pageable) {
        List<Object> objList = null;

        StringBuilder sqlStatement = new StringBuilder();
        StringBuilder sqlStatementLimit = new StringBuilder();
        List<Object> listParam = new ArrayList<Object>();
        sqlStatement.append(SQL_HISTORY_COIN_JOIN);

        sqlStatement.append(" where (uhc.transaction != 3  or (uhc.transaction =3 and uhc.txn_id is not null))  ");
        if (request.getDateFrom() != null) {
            sqlStatement.append(" and uhc.created_date >= ? ");
            listParam.add(request.getDateFrom());
        }
        if (request.getDateTo() != null) {
            sqlStatement.append(" and uhc.created_date <= ? ");
            listParam.add(request.getDateTo().plusDays(2));
        }
        if (request.getType() != null) {
            sqlStatement.append(" and uhc.type = ? ");
            listParam.add(request.getType());
        }
        if (request.getLoginId() != null) {
            sqlStatement.append(" and u.id = ? ");
            listParam.add(request.getLoginId());
        }
        if (request.getTransaction() != null) {
            sqlStatement.append(" and uhc.TRANSACTION_ID = ? ");
            listParam.add(request.getTransaction());
        }
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            sqlStatement.append(" and (LOWER(u.user_name) like LOWER(?) ");
            sqlStatement.append(" or LOWER( u.email) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.phone) like LOWER(?) ");
            sqlStatement.append(" or LOWER(u.full_name) like LOWER(?)) ");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
            listParam.add("%" + request.getSearch().trim() + "%");
        }
        sqlStatement.append(" order by uhc.id desc ");
        Query queryCount = entityManager.createNativeQuery(" select count(*) " + sqlStatement);
        for (int i = 0; i < listParam.size(); i++) {
            queryCount.setParameter(i + 1, listParam.get(i));
        }
        Integer count = ((Number) queryCount.getSingleResult()).intValue();

        sqlStatementLimit.append(" LIMIT ? OFFSET ?");
        listParam.add(pageable.getPageSize());
        listParam.add(pageable.getPageSize() * pageable.getPageNumber());
        Query query = entityManager.createNativeQuery(Constants.SQL_HISTORY_COIN + sqlStatement + sqlStatementLimit);
        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i + 1, listParam.get(i));
        }
        objList = query.getResultList();
        List<UserHistoryResult> list = new ArrayList<>();
        for (Object obj : objList) {
            UserHistoryResult result = new UserHistoryResult((Object[]) obj);
            list.add(result);
        }

        Page<UserHistoryResult> listCategory = new PageImpl<UserHistoryResult>(list, pageable, count);


        return listCategory;

    }
}

