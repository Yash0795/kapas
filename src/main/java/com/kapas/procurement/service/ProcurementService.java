package com.kapas.procurement.service;

import com.kapas.procurement.model.Token;
import com.kapas.user.entity.Role;
import com.kapas.user.entity.User;
import com.kapas.user.repository.RoleRepository;
import com.kapas.util.AppUtils;
import com.kapas.workorder.entity.Task;
import com.kapas.workorder.entity.Workflow;
import com.kapas.workorder.entity.Workorder;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.repository.TaskRepository;
import com.kapas.workorder.repository.WorkorderRepository;
import com.kapas.workorder.service.TaskService;
import com.kapas.workorder.service.WorkflowService;
import com.kapas.workorder.service.WorkorderService;
import com.kapas.workorder.util.WorkorderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

@Service
public class ProcurementService {

    private static final Logger logger = LoggerFactory.getLogger(ProcurementService.class);

    private final WorkflowService workflowService;
    private final WorkorderService workorderService;
    private final TaskService taskService;
    private final RoleRepository roleRepository;

    public ProcurementService(WorkflowService workflowService,
                              WorkorderService workorderService,
                              TaskService taskService,
                              RoleRepository roleRepository) {
        this.workflowService = workflowService;
        this.workorderService = workorderService;
        this.taskService = taskService;
        this.roleRepository = roleRepository;
    }

    public Token generateToken(String workflowId, Map<String, Object> metaData, User currentUser)
            throws NoSuchFieldException, IllegalAccessException {
        Integer workorderCount = workorderService.getCurrentDatesWorkorderCount();
        logger.info("Getting current Workorder counts: " + workorderCount);

        String tokenId = AppUtils.leftPad(String.valueOf(workorderCount + 1),
                WorkorderUtils.workorderIDLeftPaddingCount, WorkorderUtils.workorderIDLeftPaddedWith);
        logger.info("Getting Token Id: " + tokenId);

        ParsedWorkflow parsedWorkflow = workflowService.loadWorkflow(workflowId);

        Task firstTask = workorderService.createAndStartWorkorder(parsedWorkflow,
                tokenId + "_" + String.valueOf(currentUser.getId()), currentUser);

        taskService.completeTask(firstTask.getWorkorder(), parsedWorkflow, firstTask, metaData, currentUser);

        return new Token(firstTask.getWorkorder().getWorkorderId(), tokenId);
    }
}
