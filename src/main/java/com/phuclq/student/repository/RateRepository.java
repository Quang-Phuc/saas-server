package com.phuclq.student.repository;

import com.phuclq.student.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {

    List<Rate> findAllByRequestIdAndType(String requestId, String type);

    List<Rate> findAllByIdUrlAndType(String idUrl, String type);

    List<Rate> findAllByRequestIdInAndType(List<String> requestId, String type);

    List<Rate> findAllByRequestIdAndTypeAndCreatedBy(String requestId, String type, String userId);


}
