package com.phuclq.student.dto.sell;

import lombok.Data;

import java.util.List;

@Data
public class SellResultDto {
    SellDTO sell;
    List<String> urls;

}
