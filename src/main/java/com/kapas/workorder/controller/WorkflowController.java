package com.kapas.workorder.controller;

import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkflowController {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowController.class);

    private final WorkflowService workflowService;

    @GetMapping(value = "/workflow")
    public ResponseEntity<ParsedWorkflow> getWorkflow(@RequestParam String workflowId) {
        logger.debug("Getting workflowId : {}", workflowId);
        ParsedWorkflow workflow = workflowService.loadWorkflow(workflowId);

        return new ResponseEntity<>(workflow, HttpStatus.OK);
    }
}
