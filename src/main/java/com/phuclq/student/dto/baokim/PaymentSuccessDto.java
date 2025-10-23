package com.phuclq.student.dto.baokim;

import lombok.Data;

@Data
public class PaymentSuccessDto {

    String mrcOrderId;
    String created_at;
    String mrc_order_id;
    String txn_id;
    String checksum;
    String total_amount;
}
