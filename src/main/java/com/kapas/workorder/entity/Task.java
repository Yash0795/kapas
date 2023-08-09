package com.kapas.workorder.entity;


import com.kapas.user.entity.Role;
import com.kapas.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task implements Serializable {

    public enum Status {
        NOT_STARTED("NOT STARTED"),
        IN_PROGRESS("IN PROGRESS"),
        COMPLETED("COMPLETED"),
        RE_OPENED("RE OPENED"),
        CLOSED("CLOSED");

        private final String status;

        private String getStatus() {
            return this.status;
        }

        Status(String status) {
            this.status = status;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "task_id", nullable = false)
    private String taskId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workorder_id_fk", nullable = false)
    private Workorder workorder;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "remark", nullable = false)
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "completed_by", nullable = false)
    private User completedBy;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "task_number", nullable = false)
    private Integer taskNumber;

    @Column(name = "meta_data", nullable = false)
    private String metaData;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modified_by", nullable = false)
    private User modifiedBy;

    @Column(name = "creation_time", nullable = false, insertable = false)
    private Timestamp creationTime;

    @Column(name = "modification_time", nullable = false, insertable = false)
    private Timestamp modificationTime;

}
