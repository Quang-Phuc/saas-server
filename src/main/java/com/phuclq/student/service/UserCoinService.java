package com.phuclq.student.service;

import com.phuclq.student.domain.UserCoin;
import com.phuclq.student.dto.UserPaymentInfor;

public interface UserCoinService {

    UserCoin getUserCoin(Integer userId);

    boolean calculateCoin(Integer userId, Double coin);

    UserPaymentInfor getUserPaymentInfor(Integer userId);
}
