package com.phuclq.student.repository;

import com.phuclq.student.domain.UserCoinBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCoinBackupRepository extends JpaRepository<UserCoinBackup, Integer> {

    UserCoinBackup findByUserId(Integer userId);

}
