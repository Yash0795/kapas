package com.kapas.workorder.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkflowTask {

    Integer taskNumber;

    String taskId;

    String taskName;

    String description;

    String permissionName;

    Boolean isRequired;

    List<MetaDataField> fields;

}
