package com.phuclq.student.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class CategoryHomeFileResult {
    List<FileHomeDoFilterDTO> fileHomeDoFilterDTOS;
    PaginationModel paginationModel;

}
