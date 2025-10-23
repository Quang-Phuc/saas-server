package com.phuclq.student.dto.sell;

import com.phuclq.student.dto.PaginationModel;
import lombok.Data;

import java.util.List;

@Data
public class SellResultSearchDto {
    List<SellResult> list;
    PaginationModel paginationModel;

}
