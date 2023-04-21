package com.kapas.workorder.service;

import com.google.gson.Gson;
import com.kapas.user.entity.Role;
import com.kapas.user.entity.User;
import com.kapas.user.repository.RoleRepository;
import com.kapas.util.AppUtils;
import com.kapas.workorder.entity.Task;
import com.kapas.workorder.entity.Workflow;
import com.kapas.workorder.entity.Workorder;
import com.kapas.workorder.model.MetaDataField;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.model.WorkflowTask;
import com.kapas.workorder.repository.TaskRepository;
import com.kapas.workorder.repository.WorkorderRepository;
import com.kapas.workorder.util.WorkorderUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkorderService {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderService.class);

    private final WorkflowService workflowService;
    private final WorkorderRepository workorderRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;

    public Workorder createWorkorder(String workflowId, String workorderIdSuffix, User currentUser) {
        Workorder workorder = new Workorder();
        logger.info("Getting WorkorderId: {}");

        ParsedWorkflow parsedWorkflow = workflowService.loadWorkflow(workflowId);

        String workorderId = WorkorderUtils.createWorkorderId(workorderIdSuffix);
        logger.info("Getting WorkorderId: {}", workorderId);
        workorder.setWorkorderId(workorderId);

        workorder.setStatus(Workorder.Status.NOT_STARTED);
        workorder.setIsActive(true);

        Workflow workflow = new Workflow();
        workflow.setId(parsedWorkflow.getId());
        workorder.setWorkflow(workflow);
        workorder.setAssignedTo(currentUser.getRole());
        workorder.setCreatedBy(currentUser);
        workorder.setModifiedBy(currentUser);

        workorder = workorderRepository.save(workorder);

        return workorder;
    }

    public Integer getCurrentDatesWorkorderCount(Date date) {
        return workorderRepository.getCurrentDatesWorkorderCount(date);
    }


    public Task createTask(String workorderId, String taskId, String assignedToRoleName,
                           Map<String, Object> metaData, User currentUser)
            throws NoSuchFieldException {

        Workorder workorder = workorderRepository.getWorkorderByWorkorderId(workorderId);
        if(workorder == null)
            throw new NoResultException(
                    String.format("No workorder found with the given workorder id: '%s'", workorderId));
        logger.info("Getting Workorder: {}", workorder);

        Role assignedTo = roleRepository.getRoleByRoleName(assignedToRoleName);
        if(assignedTo == null)
            throw new NoResultException(
                    String.format("No Role found with the given Role Name: '%s'", assignedToRoleName));
        logger.info("Getting Role: {}", assignedTo);



        return createTask(workorder, taskId, metaData, assignedTo, currentUser);
    }

    public Task createTask(Workorder workorder, String taskId,
                           Map<String, Object> metaData, Role assignedTo,
                           User currentUser) throws NoSuchFieldException {

        ParsedWorkflow parsedWorkflow =
                AppUtils.parseJSON(workorder.getWorkflow().getWorkflowJson(), ParsedWorkflow.class);
        logger.info("Getting parsed parsedWorkflow : {} ", parsedWorkflow);

        Map<String, WorkflowTask> taskMap = WorkorderUtils.getTaskMapByTaskId(parsedWorkflow.getTaskList());
        WorkflowTask workflowTask = taskMap.get(taskId);
        logger.info("Getting workflow Task : {} ", workflowTask);

        Task task = new Task();
        task.setTaskId(taskId);
        task.setTitle(workflowTask.getTaskName());
        task.setStatus(Task.Status.IN_PROGRESS);
        task.setWorkorder(workorder);
        task.setTaskNumber(workflowTask.getTaskNumber());
        task.setIsActive(true);
        task.setAssignedTo(assignedTo);
        task.setCreatedBy(currentUser);
        task.setModifiedBy(currentUser);

        for(MetaDataField field : workflowTask.getFields()) {
            if(field.isRequired() & !metaData.containsKey(field.getName())) {
                throw new NoSuchFieldException(
                        String.format("Field '%s' is required but not found.", field.getName()));
            }
        }
        task.setMetaData(new Gson().toJson(metaData));
        task = taskRepository.save(task);
        return task;
    }


}
