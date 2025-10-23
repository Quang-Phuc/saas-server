package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentResultDto {
    List<PaymentResult> list;
    PaginationModel paginationModel;

}
