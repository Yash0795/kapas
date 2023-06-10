package com.kapas.workorder.util;

import com.kapas.util.AppUtils;
import com.kapas.workorder.model.WorkflowTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkorderUtils {

    Logger logger = LoggerFactory.getLogger(WorkorderUtils.class);

    public static String workorderIdPrefix = "WO";
    public static Integer workorderIDLeftPaddingCount = 3;

    public static String workorderIDLeftPaddedWith = "0";

    public static String workorderIDDateFormate = "ddMMyyyy";



    public static String createWorkorderId(String suffix) {
        String workorderId = workorderIdPrefix;
        workorderId += "-" + AppUtils.todaysDateString(workorderIDDateFormate);
        if(suffix != null) workorderId += "-" + suffix;
        return workorderId;
    }

    public static Map<String, WorkflowTask> getTaskMapByTaskId(List<WorkflowTask> taskList) {
        return taskList.stream().collect(Collectors.toMap(WorkflowTask::getTaskId,
                        workflowTask -> workflowTask));
    }

    public static Map<Integer, WorkflowTask> getTaskMapByTaskNumber(List<WorkflowTask> taskList) {
        return taskList.stream().collect(Collectors.toMap(WorkflowTask::getTaskNumber,
                workflowTask -> workflowTask));
    }

    public static String createTaskId(String workorderId, String taskId) {
        return workorderId + "-" + taskId;
    }

    public static String getTaskIdSuffix(String taskId) {
        String[] taskIdArray = taskId.split("-");
        return taskIdArray[taskIdArray.length-1];
    }
}
