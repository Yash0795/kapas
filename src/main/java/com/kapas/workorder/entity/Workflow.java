package com.kapas.workorder.entity;


import com.kapas.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"createdBy","modifiedBy"})
@Table(name = "workflow")
public class Workflow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "workflow_id", nullable = false)
    private String workflowId;

    private String description;

    private Integer version;

    @Column(name = "workflow_json", nullable = false)
    private String workflowJson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modified_by", nullable = false)
    private User modifiedBy;

    @Column(name = "creation_time", nullable = false)
    private Timestamp creationTime;

    @Column(name = "modification_time", nullable = false)
    private Timestamp modificationTime;

}
