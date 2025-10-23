package com.phuclq.student.repository;

import com.phuclq.student.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findAllByTitleContainingIgnoreCase(String search, Pageable pageable);

    Sale findAllByStatus(Boolean status);

    @Query("SELECT e FROM SALE e WHERE e.status =true  and  e.endDate >= :currentDateTime")
    Sale findAllByEndDateBeforeOrEqual(@Param("currentDateTime") LocalDateTime currentDateTime);

}

