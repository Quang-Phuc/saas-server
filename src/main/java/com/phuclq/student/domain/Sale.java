package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "SALE")
@Getter
@Setter
@Builder
@Table(name = "SALE")
public class Sale extends Auditable<String> {


    @Column(name = "START_DATE", nullable = false)
    LocalDateTime startDate;
    @Column(name = "END_DATE", nullable = false)
    LocalDateTime endDate;
    @Id
    @SequenceGenerator(name = "SALE_SEQUENCE", sequenceName = "SALE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "STATUS")
    private Boolean status;

    @Column(name = "URL", columnDefinition = "LONGTEXT")
    private String url;


}
