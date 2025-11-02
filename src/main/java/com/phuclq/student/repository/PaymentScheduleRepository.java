package com.phuclq.student.repository;
import com.phuclq.student.domain.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {

    // Lấy tất cả lịch trả theo hợp đồng
    List<PaymentSchedule> findByContractId(Long contractId);

    // Lấy lịch trả theo trạng thái (ví dụ: PENDING, PAID)
    List<PaymentSchedule> findByContractIdAndStatus(Long contractId, String status);

    // Xóa tất cả lịch trả của 1 hợp đồng (nếu cần update lại)
    void deleteByContractId(Long contractId);
}
