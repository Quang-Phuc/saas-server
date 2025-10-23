package com.phuclq.student.dto;

import lombok.Data;

@Data
public class OrderDto {

    Integer bpmId;
    private Integer totalAmount;
    private String urlCallBack;

    @Override
    public String toString() {
        return "OrderDto{" +
                "bpmId=" + bpmId +
                ", totalAmount=" + totalAmount +
                ", urlCallBack='" + urlCallBack + '\'' +
                '}';
    }
}
