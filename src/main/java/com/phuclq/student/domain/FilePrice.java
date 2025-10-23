package com.phuclq.student.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_price")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilePrice extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "FILE_ID")
    private Integer fileId;

    @Column(name = "PRICE")
    private Double price;

    public FilePrice(Integer fileId, Double price, Integer userId) {
        this.fileId = fileId;
        this.price = price;
        this.setCreatedBy(userId.toString());
        this.setCreatedDate(LocalDateTime.now());
    }


}
