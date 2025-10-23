package com.phuclq.student.dto;

import lombok.Data;

@Data
public class CategoryHomeDTO {
    private String nameCategory;
    private Integer countCategory;
    private Long id;

    public CategoryHomeDTO(CategoryHomeResult categoryHomeResult) {
        this.nameCategory = categoryHomeResult.getName();
        ;
        this.countCategory = categoryHomeResult.getCountCategory();
        this.id = categoryHomeResult.getId();
    }

    public CategoryHomeDTO() {
    }
}
