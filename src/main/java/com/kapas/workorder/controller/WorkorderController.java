package com.kapas.workorder.controller;

import com.kapas.user.entity.User;
import com.kapas.util.Constants;
import com.kapas.workorder.entity.Task;
import com.kapas.workorder.entity.Workorder;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.service.WorkflowService;
import com.kapas.workorder.service.WorkorderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class WorkorderController {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderController.class);

    private final WorkorderService workorderService;

    public WorkorderController(WorkorderService workorderService) {
        this.workorderService = workorderService;
    }

    @GetMapping(value = "/createWorkorder")
    public ResponseEntity<Workorder> createWorkorder(String workflowId,
                                                     String workorderIdSuffix,
                                                     HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting workflowId : {}, workorderIdSuffix : {}, currentUserId: {}", workflowId,
                workorderIdSuffix, currentUser.getId());
        Workorder workorder = workorderService.createWorkorder(workflowId, workorderIdSuffix, currentUser);
        return new ResponseEntity<>(workorder, HttpStatus.OK);
    }

    @PostMapping(value = "/createTask")
    public ResponseEntity<Task> createTask(String workorderId,
                                           String taskId,
                                           String assignedToRoleName,
                                           @RequestBody Map<String, Object> metaData,
                                           HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting workorderId : {}, taskId : {}, assignedToRoleName : {}, " +
                        "metaData : {}, currentUserId: {}", workorderId, taskId, assignedToRoleName,
                metaData, currentUser.getId());
        Task task = workorderService.createTask(workorderId, taskId, assignedToRoleName, metaData, currentUser);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }



}
