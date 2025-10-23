package com.phuclq.student.domain;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "REQUEST_HISTORY")
@Getter
@Setter
@Builder
@Table(name = "REQUEST_HISTORY")
public class RequestHistory {

    @Id
    @SequenceGenerator(name = "REQUEST_HISTORY_SEQUENCE", sequenceName = "REQUEST_HISTORY_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_HISTORY_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column
    private Date createdTime;
    @Column(columnDefinition = "LONGTEXT")
    private String url;
    @Column(columnDefinition = "LONGTEXT")
    private String requestContent;
    @Column(columnDefinition = "LONGTEXT")
    private String responseContent;
    @Column(columnDefinition = "LONGTEXT")
    private String responseCode;
    @Column
    private String status;
    @Column
    private String type;
    private long duration;
    @Column
    private Integer requestId;


}
