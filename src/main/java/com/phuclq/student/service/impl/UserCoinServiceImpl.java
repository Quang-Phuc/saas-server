package com.phuclq.student.service.impl;

import com.phuclq.student.domain.UserCoin;
import com.phuclq.student.domain.UserCoinBackup;
import com.phuclq.student.dto.UserPaymentInfor;
import com.phuclq.student.repository.UserCoinBackupRepository;
import com.phuclq.student.repository.UserCoinRepository;
import com.phuclq.student.service.UserCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserCoinServiceImpl implements UserCoinService {
    @Autowired
    private UserCoinRepository userCoinRepository;

    @Autowired
    private UserCoinBackupRepository userCoinBackupRepository;

    @Override
    public UserCoin getUserCoin(Integer userId) {
        UserCoin byUserId = userCoinRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoin(userId, 0D);
    }

    public UserCoin findByUserId(Integer userId) {
        UserCoin byUserId = userCoinRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoin(userId, 0D);
    }

    public UserCoinBackup findByUserIdBackUp(Integer userId) {
        UserCoinBackup byUserId = userCoinBackupRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoinBackup(userId, 0D);
    }

    @Override
    public boolean calculateCoin(Integer userId, Double coin) {
        UserCoin userCoin = findByUserId(userId);
        UserCoinBackup userCoinBackup = findByUserIdBackUp(userId);
        if (userCoin != null) {
            Double totalCoin = userCoin.getTotalCoin() == null ? userCoin.getTotalCoin() : 0d;
            totalCoin -= coin;
            userCoin.setTotalCoin(totalCoin);
            userCoinBackup.setTotalCoin(totalCoin);
            userCoinRepository.save(userCoin);
            userCoinBackupRepository.save(userCoinBackup);
            return true;
        }
        return false;
    }

    @Override
    public UserPaymentInfor getUserPaymentInfor(Integer userId) {
        return userCoinRepository.getUserCoinInfor(userId);
    }

}
