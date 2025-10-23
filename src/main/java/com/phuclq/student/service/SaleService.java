package com.phuclq.student.service;

import com.phuclq.student.domain.Sale;
import com.phuclq.student.dto.sale.SaleRequest;
import com.phuclq.student.dto.sale.SaleResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface SaleService {


    Long creatOrUpdate(SaleRequest dto) throws IOException;

    Page<Sale> search(Pageable pageable, String search);

    SaleResultDto findAllById(Long id);

    SaleResultDto findAllStatus();

    void deleteById(Long id);

}
