package com.phuclq.student.dto.school;

import com.phuclq.student.domain.School;
import com.phuclq.student.dto.AttachmentDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class SchoolResultDetail {
    School school;
    List<AttachmentDTO> attachmentDTOList;
    List<String> urls;
    private Double totalRateUser;
    private Double totalRate ;

}
