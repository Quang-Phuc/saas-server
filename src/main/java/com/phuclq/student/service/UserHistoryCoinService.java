package com.phuclq.student.service;

import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.UserHistoryCoinResultDto;
import com.phuclq.student.dto.UserHistoryCoinResultTotalDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserHistoryCoinService {
    UserHistoryCoinResultDto getHistoryTransaction(FileHomePageRequest historyCoinRequest, Pageable pageable);

    List<UserHistoryCoinResultTotalDto> getHistoryTransactionTotal();

    void deleteHistoryCoin(Integer id);
}
