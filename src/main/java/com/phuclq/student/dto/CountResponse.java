package com.phuclq.student.dto;

import lombok.Data;

@Data
public class CountResponse {
    private final int total;
    private final int approved;
    private final int unapproved;

    public CountResponse(int total, int approved, int unapproved) {
        this.total = total;
        this.approved = approved;
        this.unapproved = unapproved;
    }

}
