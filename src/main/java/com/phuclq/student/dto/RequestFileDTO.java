package com.phuclq.student.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestFileDTO {

    @NotNull(message = "name is mandatory")
    @NotBlank(message = "name is mandatory")
    private String name;

    private String type;

    @NotNull(message = "content is mandatory")
    @NotBlank(message = "content is mandatory")
    private String content;

    @NotNull(message = "extension is mandatory")
    @NotBlank(message = "extension is mandatory")
    private String extension;

    private String attachmentTypeCode;
}
