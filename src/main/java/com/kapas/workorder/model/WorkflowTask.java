package com.kapas.workorder.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkflowTask {

    private Integer taskNumber;

    private String taskId;

    private String taskName;

    private String description;

    private String assignedToRoleName;

    private Boolean isRequired;

    private List<MetaDataField> fields;

}
