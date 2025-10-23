package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "NOTIFICATION")
public class Notification extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "NOTIFICATION_SEQUENCE", sequenceName = "NOTIFICATION_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NOTIFICATION_CODE")
    private Integer notificationCode;

    @Column(name = "IS_READ")
    private Boolean isRead;

    @Column(name = "IS_VIEW")
    private Boolean isView;

    @Column(name = "ASSIGNEE")
    private String assignee;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "message", columnDefinition = "LONGTEXT")
    private String message;

    @Column(name = "IMAGE_ICON", columnDefinition = "LONGTEXT")
    private String imageIcon;

    @Column(name = "URL_DETAIL", columnDefinition = "LONGTEXT")
    private String urlDetail;


    public Notification(Integer notificationCode,
                        String assignee, String type, String status, String message, String imageIcon, String urlDetail) {
        this.notificationCode = notificationCode;
        this.isRead = false;
        this.isView = false;
        this.assignee = assignee;
        this.type = type;
        this.status = status;
        this.setCreatedDate(LocalDateTime.now());
        this.setCreatedBy("SYSTEM");
        this.message = message;
        this.imageIcon = imageIcon;
        this.urlDetail = urlDetail;
    }
}
