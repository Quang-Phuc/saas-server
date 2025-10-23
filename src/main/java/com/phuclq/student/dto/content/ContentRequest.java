package com.phuclq.student.dto.content;

import com.phuclq.student.dto.RequestFileDTO;
import lombok.Data;

import java.util.List;

@Data
public class ContentRequest {

    List<RequestFileDTO> files;
    private Long id;
    private String title;
    private String type;


}
