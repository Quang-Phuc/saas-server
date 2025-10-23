package com.phuclq.student.dto.webhook;

import com.phuclq.student.domain.Attachment;
import com.phuclq.student.dto.HomeDetailDTO;
import lombok.Data;

import java.util.List;

@Data
public class HomeDto {
    private HomeDetailDTO allById;
    private List<Attachment> attachments;
}
