package com.phuclq.student.repository;

import com.phuclq.student.domain.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import com.phuclq.student.domain.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LoanCustomRepositoryImpl implements LoanCustomRepository {

    private final EntityManager em;

    @Override
    public Page<Loan> searchLoans(String keyword, String status, LocalDate fromDate,
                                  LocalDate toDate, Long employeeId, Pageable pageable) {

        StringBuilder sql = new StringBuilder("SELECT l FROM Loan l WHERE 1=1 ");

        if (keyword != null && !keyword.isEmpty()) {
            sql.append("AND (LOWER(l.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
                    .append("OR LOWER(l.customer.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))) ");
        }
        if (status != null && !status.equalsIgnoreCase("Tất cả")) {
            sql.append("AND l.status = :status ");
        }
        if (fromDate != null) {
            sql.append("AND l.loanDate >= :fromDate ");
        }
        if (toDate != null) {
            sql.append("AND l.loanDate <= :toDate ");
        }
        if (employeeId != null) {
            sql.append("AND l.employee.id = :employeeId ");
        }

        TypedQuery<Loan> query = em.createQuery(sql.toString(), Loan.class);
        TypedQuery<Loan> countQuery = em.createQuery(sql.toString().replace("SELECT l", "SELECT COUNT(l)"), Loan.class);

        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword", keyword);
            countQuery.setParameter("keyword", keyword);
        }
        if (status != null && !status.equalsIgnoreCase("Tất cả")) {
            query.setParameter("status", status);
            countQuery.setParameter("status", status);
        }
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
            countQuery.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
            countQuery.setParameter("toDate", toDate);
        }
        if (employeeId != null) {
            query.setParameter("employeeId", employeeId);
            countQuery.setParameter("employeeId", employeeId);
        }

        long total = countQuery.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Loan> result = query.getResultList();

        return new PageImpl<>(result, pageable, total);
    }
}

