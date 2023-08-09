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

    Integer id;

    String workflowId;

    Integer version;

    String description;

    Boolean autoComplete;

    Integer totalTask;

    List<WorkflowTask> taskList;

    String permissionName;

}
