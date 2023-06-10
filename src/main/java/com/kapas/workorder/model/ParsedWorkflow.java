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

    private Boolean autoComplete;

    private Integer totalTask;

    private List<WorkflowTask> taskList;

    private String assignedToRoleName;

    private User createdBy;

    private User modifiedBy;

    private Timestamp creationTime;

    private Timestamp modificationTime;

}
