package com.kapas.workorder.model;

import com.kapas.user.entity.User;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParsedWorkflow {

    private Integer id;

    private String workflowId;

    private Integer version;

    private String description;

    private Integer totalTask;

    private List<WorkflowTask> taskList;

    private User createdBy;

    private User modifiedBy;

    private Timestamp creationTime;

    private Timestamp modificationTime;

}
