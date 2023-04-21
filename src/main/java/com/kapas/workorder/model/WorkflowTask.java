package com.kapas.workorder.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkflowTask {

    private int taskNumber;

    private String taskId;

    private String taskName;

    private String description;

    private boolean isRequired;

    private List<MetaDataField> fields;

}
