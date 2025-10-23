package com.phuclq.student.service;

import com.phuclq.student.domain.SellCategory;
import com.phuclq.student.domain.UserHistorySell;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.sell.SellRequest;
import com.phuclq.student.dto.sell.SellResultDto;
import com.phuclq.student.dto.sell.SellResultSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface SellService {

    Long creatOrUpdate(SellRequest request) throws IOException;


    Page<SellCategory> search(Pageable pageable, String search);

    SellResultSearchDto searchSell(Boolean admin, Pageable pageable, SellRequest search);

    SellResultDto findAllById(String idUrlCategory, String idUrl);

    void deleteById(Long id);

    UserHistorySell activityHome(Long id, Integer activity);

    void deleteActivityHome(Long id, Integer card);

    SellResultSearchDto myHome(SellRequest request, Pageable pageable);

    TotalMyDTO total();


    void approve(Long id);

    Page<HistoryFileResult> deleteHistory(SellRequest request);
}
