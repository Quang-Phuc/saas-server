package com.phuclq.student.dto;

import com.phuclq.student.domain.PaymentRequest;
import lombok.Data;

import java.util.List;

@Data
public class PaymentDTO {
    private PaymentRequest paymentRequest;
    private List<AttachmentDTO> attachmentDTO;
}
