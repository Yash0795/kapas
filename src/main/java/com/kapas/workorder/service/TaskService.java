package com.kapas.workorder.service;

import com.google.gson.Gson;
import com.kapas.user.entity.Role;
import com.kapas.user.entity.User;
import com.kapas.user.repository.RoleRepository;
import com.kapas.util.AppUtils;
import com.kapas.workorder.entity.Task;
import com.kapas.workorder.entity.Workorder;
import com.kapas.workorder.model.MetaDataField;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.model.WorkflowTask;
import com.kapas.workorder.repository.TaskRepository;
import com.kapas.workorder.repository.WorkorderRepository;
import com.kapas.workorder.util.WorkorderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final WorkflowService workflowService;
    private final WorkorderService workorderService;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;

    public TaskService(WorkflowService workflowService,
                       WorkorderService workorderService,
                            TaskRepository taskRepository,
                            RoleRepository roleRepository) {
        this.workflowService = workflowService;
        this.workorderService = workorderService;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
    }

    public Task createTask(String workorderId, String taskId, User currentUser) {

        Workorder workorder = workorderService.getWorkorderByWorkorderId(workorderId);
        if(workorder == null)
            throw new NoResultException(
                    String.format("No workorder found with the given workorder id: '%s'", workorderId));
        logger.info("Getting Workorder: {}", workorder);

        validateIfWorkorderNotCompleted(workorder);

        return createTask(workorder, taskId, currentUser);
    }

    public Task createTask(Workorder workorder, String taskId, User currentUser) {

        ParsedWorkflow parsedWorkflow =
                AppUtils.parseJSON(workorder.getWorkflow().getWorkflowJson(), ParsedWorkflow.class);
        logger.info("Getting parsed parsedWorkflow : {} ", parsedWorkflow);

        return createTask(parsedWorkflow, workorder, taskId, currentUser);
    }

    public Task createTask(ParsedWorkflow parsedWorkflow, Workorder workorder, String taskId, User currentUser) {

        Map<String, WorkflowTask> taskMap = WorkorderUtils.getTaskMapByTaskId(parsedWorkflow.getTaskList());
        WorkflowTask workflowTask = taskMap.get(taskId);
        logger.info("Getting workflow Task : {} ", workflowTask);

        Role assignedTo = roleRepository.getRoleByRoleName(workflowTask.getAssignedToRoleName());
        if(assignedTo == null)
            throw new NoResultException(String.format("No Role found with the given Role Name: '%s'",
                    workflowTask.getAssignedToRoleName()));
        logger.info("Getting Assigned To Role: {}", assignedTo);

        Task task = new Task();
        task.setTaskId(WorkorderUtils.createTaskId(workorder.getWorkorderId(), taskId));
        task.setTitle(workflowTask.getTaskName());
        task.setStatus(Task.Status.NOT_STARTED);
        task.setWorkorder(workorder);
        task.setTaskNumber(workflowTask.getTaskNumber());
        task.setIsActive(true);
        task.setAssignedTo(assignedTo);
        task.setCreatedBy(currentUser);
        task.setModifiedBy(currentUser);

        task = taskRepository.save(task);
        return task;
    }

    public void completeTask(String taskId, Map<String, Object> taskData,
                             User currentUser) throws NoSuchFieldException, IllegalAccessException {

        Task task = taskRepository.getTaskByTaskId(taskId);
        if(task == null)
            throw new NoResultException(
                    String.format("No Task found with the given task id: '%s'", task));
        logger.info("Getting Task: {}", task);

        if(!task.getAssignedTo().equals(currentUser.getRole()))
            throw new IllegalAccessException(
                    String.format("Task is not assigned to the current User. User ID : {}",
                            currentUser.getId()));

        completeTask(task.getWorkorder(), task, taskData, currentUser);
    }

    public void completeTask(Workorder workorder, Task task, Map<String, Object> taskData,
                             User currentUser) throws NoSuchFieldException, IllegalAccessException {

        validateIfWorkorderNotCompleted(workorder);

        ParsedWorkflow parsedWorkflow =
                AppUtils.parseJSON(workorder.getWorkflow().getWorkflowJson(), ParsedWorkflow.class);

        completeTask(workorder, parsedWorkflow, task, taskData, currentUser);
    }

    private void validateIfWorkorderNotCompleted(Workorder workorder) {
        if(workorder.getStatus().equals(Workorder.Status.COMPLETED)
                && workorder.getStatus().equals(Workorder.Status.CLOSED))
            throw new UnsupportedOperationException("Workorder is already completed or closed.");
    }

    public void completeTask(Workorder workorder, ParsedWorkflow parsedWorkflow,
                             Task task, Map<String, Object> taskData,
                             User currentUser) throws NoSuchFieldException, IllegalAccessException,
            UnsupportedOperationException{

        if(!task.getAssignedTo().equals(currentUser.getRole()))
            throw new IllegalAccessException(
                    String.format("Task is not assigned to the current User. User ID : {}",
                            currentUser.getId()));

        validateIfTaskNotCompleted(task);

        Map<String, WorkflowTask> taskMap = WorkorderUtils.getTaskMapByTaskId(parsedWorkflow.getTaskList());
        WorkflowTask workflowTask = taskMap.get(WorkorderUtils.getTaskIdSuffix(task.getTaskId()));

        validateTaskMetaFields(workflowTask, taskData);

        task.setMetaData(new Gson().toJson(taskData));
        task.setStatus(Task.Status.COMPLETED);
        task.setModifiedBy(currentUser);
        taskRepository.save(task);

        completeWorkorderOrCreateNextTask(parsedWorkflow, workflowTask, workorder, currentUser);
    }

    private void validateIfTaskNotCompleted(Task task) {
        if(task.getStatus().equals(Task.Status.COMPLETED)
                && task.getStatus().equals(Task.Status.CLOSED))
            throw new UnsupportedOperationException("Task is already completed or closed.");
    }

    private void validateTaskMetaFields(WorkflowTask workflowTask, Map<String, Object> taskData) throws NoSuchFieldException {
        for(MetaDataField field : workflowTask.getFields()) {
            if(field.getIsRequired() && !taskData.containsKey(field.getName())) {
                throw new NoSuchFieldException(
                        String.format("Field '%s' is required but not found.", field.getName()));
            }
        }
    }

    public void completeWorkorderOrCreateNextTask(ParsedWorkflow parsedWorkflow, WorkflowTask workflowTask,
                                                   Workorder workorder, User currentUser) throws NoSuchFieldException {
        if(parsedWorkflow.getAutoComplete() && workflowTask.getTaskNumber() == parsedWorkflow.getTotalTask()) {
            workorderService.completeWorkorder(workorder, currentUser);
        } else {
            Map<Integer, WorkflowTask> taskNumberTaskMap =
                    WorkorderUtils.getTaskMapByTaskNumber(parsedWorkflow.getTaskList());
            WorkflowTask nextWorkflowTask = taskNumberTaskMap.get(workflowTask.getTaskNumber() + 1);
            logger.info("Getting nextWorkflowTask Task : {} ", nextWorkflowTask);
            createTask(workorder, nextWorkflowTask.getTaskId(), currentUser);
        }
    }

    public void startTask(String taskId, Map<String, Object> taskData, String remark, User currentUser) {
        Task task = taskRepository.getTaskByTaskId(taskId);
        if(task == null)
            throw new NoResultException(
                    String.format("No Task found with the given task id: '%s'", taskId));
        logger.info("Getting Task: {}", taskId);
        startTask(task.getWorkorder(), task, taskData, remark, currentUser);
    }

    public void startTask(Workorder workorder, Task task, Map<String, Object> taskData,
                          String remark, User currentUser) {
        task.setStatus(Task.Status.IN_PROGRESS);
        if(remark != null) task.setRemark(remark);
        if(taskData != null) task.setMetaData(new Gson().toJson(taskData));
        task.setModifiedBy(currentUser);
        taskRepository.save(task);
        workorder.setModifiedBy(currentUser);
        workorderService.save(workorder);
    }

    public void closeTask(String taskId, String remark, User currentUser) {
        Task task = taskRepository.getTaskByTaskId(taskId);
        if(task == null)
            throw new NoResultException(
                    String.format("No Task found with the given task id: '%s'", taskId));
        logger.info("Getting Task: {}", taskId);
        closeTask(task.getWorkorder(), task, remark, currentUser);
    }

    public void closeTask(Workorder workorder, Task task, String remark, User currentUser) {
        task.setStatus(Task.Status.CLOSED);
        task.setModifiedBy(currentUser);
        if(remark != null) task.setRemark(remark);
        taskRepository.save(task);
        workorder.setModifiedBy(currentUser);
        workorderService.save(workorder);
    }

    public List<Task> getCurrentUserTasks(User currentUser) {
        Role role = currentUser.getRole();
        return taskRepository.getTasksByRole(role);
    }
}