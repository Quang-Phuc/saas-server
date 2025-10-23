package com.phuclq.student.dto.sale;

import com.phuclq.student.domain.Attachment;
import com.phuclq.student.domain.Sale;
import lombok.Data;

import java.util.List;

@Data
public class SaleResultDto {
    Sale sale;
    String url;
    String content;
    List<Attachment> attachments;

}
