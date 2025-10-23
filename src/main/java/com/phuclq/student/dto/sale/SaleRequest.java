package com.phuclq.student.dto.sale;

import com.phuclq.student.dto.RequestFileDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleRequest {

    List<RequestFileDTO> files;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Long id;
    String title;
    String content;
    Boolean isDeleted;
    Boolean status;


}
