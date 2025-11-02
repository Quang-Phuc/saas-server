package com.phuclq.student.repository;

// (Đặt trong package ...repository)
import com.phuclq.student.domain.FeeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeDetailRepository extends JpaRepository<FeeDetail, Long> {
}
