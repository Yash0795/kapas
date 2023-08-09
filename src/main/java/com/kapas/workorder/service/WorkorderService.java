package com.kapas.workorder.service;


import com.kapas.user.entity.Role;
import com.kapas.user.entity.User;
import com.kapas.user.repository.RoleRepository;
import com.kapas.user.service.PermissionService;
import com.kapas.workorder.entity.Task;
import com.kapas.workorder.entity.Workflow;
import com.kapas.workorder.entity.Workorder;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.model.WorkflowTask;
import com.kapas.workorder.repository.WorkorderRepository;
import com.kapas.workorder.util.WorkorderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Map;

@Service
public class WorkorderService {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderService.class);

    private final WorkflowService workflowService;
    private final TaskService taskService;
    private final WorkorderRepository workorderRepository;
    private final PermissionService permissionService;
    public WorkorderService(WorkflowService workflowService,
                            TaskService taskService,
                            WorkorderRepository workorderRepository,
                            PermissionService permissionService) {
        this.workflowService = workflowService;
        this.taskService = taskService;
        this.workorderRepository = workorderRepository;
        this.permissionService = permissionService;

    }

    public Workorder createWorkorder(String workflowId, String workorderIdSuffix, User currentUser) {
        ParsedWorkflow parsedWorkflow = workflowService.loadWorkflow(workflowId);
        return createWorkorder(parsedWorkflow, workorderIdSuffix, currentUser);
    }

    public Task createAndStartWorkorder(String workflowId, String workorderIdSuffix, User currentUser) {
        ParsedWorkflow parsedWorkflow = workflowService.loadWorkflow(workflowId);
        Workorder workorder = createWorkorder(parsedWorkflow, workorderIdSuffix, currentUser);
        Map<Integer, WorkflowTask> taskNumberTaskMap =
                WorkorderUtils.getTaskMapByTaskNumber(parsedWorkflow.getTaskList());
        WorkflowTask firstWorkflowTask = taskNumberTaskMap.get(1);
        return taskService.createTask(parsedWorkflow, workorder, firstWorkflowTask.getTaskId(), currentUser);
    }

    public Task createAndStartWorkorder(ParsedWorkflow parsedWorkflow, String workorderIdSuffix, User currentUser) {
        Workorder workorder = createWorkorder(parsedWorkflow, workorderIdSuffix, currentUser);
        Map<Integer, WorkflowTask> taskNumberTaskMap =
                WorkorderUtils.getTaskMapByTaskNumber(parsedWorkflow.getTaskList());
        WorkflowTask firstWorkflowTask = taskNumberTaskMap.get(1);
        startWorkorder(workorder, currentUser);
        return taskService.createTask(parsedWorkflow, workorder, firstWorkflowTask.getTaskId(), currentUser);
    }

    public Workorder createWorkorder(ParsedWorkflow parsedWorkflow, String workorderIdSuffix, User currentUser) {

        boolean hasPermission = permissionService.hasPermission(currentUser.getRole(),
                parsedWorkflow.getPermissionName());
        if(!hasPermission)
            throw new UnsupportedOperationException(String.format("Current user has no permission to perform " +
                    "the requested operation."));

        Workorder workorder = new Workorder();

        String workorderId = WorkorderUtils.createWorkorderId(workorderIdSuffix);
        logger.info("Getting WorkorderId: {}", workorderId);
        workorder.setWorkorderId(workorderId);

        workorder.setStatus(Workorder.Status.NOT_STARTED);
        workorder.setIsActive(true);
        Workflow workflow = new Workflow();
        workflow.setId(parsedWorkflow.getId());
        workorder.setWorkflow(workflow);
        workorder.setCreatedBy(currentUser);
        workorder.setModifiedBy(currentUser);
        return workorderRepository.save(workorder);
    }

    public Integer getCurrentDatesWorkorderCount() {
        return workorderRepository.getCurrentDatesWorkorderCount();
    }

    public void startWorkorder(String workorderId, User currentUser) {
        Workorder workorder = getWorkorderByWorkorderId(workorderId);
        if(workorder == null)
            throw new NoResultException(
                    String.format("No workorder found with the given workorder id: '%s'", workorderId));
        startWorkorder(workorder, currentUser);
    }

    public void startWorkorder(Workorder workorder, User currentUser) {
        workorder.setStatus(Workorder.Status.IN_PROGRESS);
        workorder.setModifiedBy(currentUser);
        workorderRepository.save(workorder);
    }

    public void completeWorkorder(String workorderId, User currentUser) {
        Workorder workorder = getWorkorderByWorkorderId(workorderId);
        if(workorder == null)
            throw new NoResultException(
                    String.format("No workorder found with the given workorder id: '%s'", workorderId));
        completeWorkorder(workorder, currentUser);
    }

    public void completeWorkorder(Workorder workorder, User currentUser) {
        workorder.setStatus(Workorder.Status.COMPLETED);
        workorder.setModifiedBy(currentUser);
        workorderRepository.save(workorder);
    }

    public void closeWorkorder(String workorderId, User currentUser) {
        Workorder workorder = getWorkorderByWorkorderId(workorderId);
        if(workorder == null)
            throw new NoResultException(
                    String.format("No workorder found with the given workorder id: '%s'", workorderId));
        closeWorkorder(workorder, currentUser);
    }

    public void closeWorkorder(Workorder workorder, User currentUser) {
        workorder.setStatus(Workorder.Status.CLOSED);
        workorder.setModifiedBy(currentUser);
        workorderRepository.save(workorder);
    }

    public Workorder getWorkorderByWorkorderId(String workorderId) {
        return workorderRepository.getWorkorderByWorkorderId(workorderId);
    }

    public Workorder save(Workorder workorder) {
        return workorderRepository.save(workorder);
    }
}
