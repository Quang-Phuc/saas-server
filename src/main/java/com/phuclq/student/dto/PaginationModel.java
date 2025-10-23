package com.phuclq.student.dto;

import lombok.Data;

@Data
public class PaginationModel {
    private int pageNumber;
    private int pageSize;
    private int count;

    public PaginationModel(final int pageNumber, final int pageSize, final int count) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.count = count;
    }
}
