package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResultDto {
    List<UserAdminResult> list;
    PaginationModel paginationModel;

}
