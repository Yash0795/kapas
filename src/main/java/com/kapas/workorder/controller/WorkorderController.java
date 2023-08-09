package com.kapas.workorder.controller;

import com.kapas.user.entity.User;
import com.kapas.util.Constants;
import com.kapas.workorder.entity.Task;
import com.kapas.workorder.entity.Workorder;
import com.kapas.workorder.service.WorkorderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WorkorderController {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderController.class);

    private final WorkorderService workorderService;

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

    @GetMapping(value = "/createAndStartWorkorder")
    public ResponseEntity<Task> createAndStartWorkorder(String workflowId,
                                                     String workorderIdSuffix,
                                                     HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting workflowId : {}, workorderIdSuffix : {}, currentUserId: {}", workflowId,
                workorderIdSuffix, currentUser.getId());
        Task task = workorderService.createAndStartWorkorder(workflowId, workorderIdSuffix, currentUser);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping(value = "/startWorkorder")
    public ResponseEntity<String> startWorkorder(String workorderId,
                                                        HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting workorderId : {}, currentUserId: {}",
                workorderId, currentUser.getId());
        workorderService.startWorkorder(workorderId, currentUser);
        return new ResponseEntity<>("Workorder is started.", HttpStatus.OK);
    }

    @GetMapping(value = "/closeWorkorder")
    public ResponseEntity<String> closeWorkorder(String workorderId,
                                                 HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting workorderId : {}, currentUserId: {}",
                workorderId, currentUser.getId());
        workorderService.closeWorkorder(workorderId, currentUser);
        return new ResponseEntity<>("Workorder is closed.", HttpStatus.OK);
    }

    @GetMapping(value = "/completeWorkorder")
    public ResponseEntity<String> completeWorkorder(String workorderId,
                                                 HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting workorderId : {}, currentUserId: {}",
                workorderId, currentUser.getId());
        workorderService.completeWorkorder(workorderId, currentUser);
        return new ResponseEntity<>("Workorder is completed.", HttpStatus.OK);
    }

}
