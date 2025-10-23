package com.phuclq.student.dto.banner;

import com.phuclq.student.domain.Attachment;
import com.phuclq.student.domain.Banner;
import lombok.Data;

import java.util.List;

@Data
public class BannerResultDto {
    Banner banner;
    List<String> urls;
    List<Attachment> attachments;
}
