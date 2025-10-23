package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user_history_file")
public class UserHistoryFile extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_hisoty_id")
    private Integer userHisotyId;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "activity_date")
    private Timestamp activityDate;

    @Column(name = "is_file_update")
    private Boolean isFileUpdate;

    public UserHistoryFile() {
    }

    public UserHistoryFile(Integer userHisotyId, Integer fileId, Timestamp activityDate) {
        this.userHisotyId = userHisotyId;
        this.fileId = fileId;
        this.activityDate = activityDate;
    }
}
