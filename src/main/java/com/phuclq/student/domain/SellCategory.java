package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "SELL_CATEGORY")
@Getter
@Setter
@Builder
@Table(name = "SELL_CATEGORY")
public class SellCategory extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "NAME")
    private String name;

}
