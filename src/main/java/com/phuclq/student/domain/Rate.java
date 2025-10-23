package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "RATE")
@Getter
@Setter
@Builder
@Table(name = "RATE")
public class Rate extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "RATE_SEQUENCE", sequenceName = "RATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RATE_SEQUENCE")
    @Column(name = "ID")
    private Long id;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "RATE")
    private Double rate;

    @Column(name = "TYPE")
    private String type;


}
