package com.phuclq.student.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "WARNING")
public class Warning extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "detail")
    private String detail;

    public Warning() {
        this.setLastUpdatedDate(LocalDateTime.now());
        this.setCreatedBy("system");
        this.setCreatedDate(LocalDateTime.now());
    }


}
