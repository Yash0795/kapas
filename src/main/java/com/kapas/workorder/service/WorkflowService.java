package com.kapas.workorder.service;

import com.kapas.util.AppUtils;
import com.kapas.workorder.entity.Workflow;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);

    private final WorkflowRepository workflowRepository;

    public ParsedWorkflow loadWorkflow(String workflowId){

        Workflow workflow =
                workflowRepository.getLatestWorkflowByWorkflowId(workflowId);
        logger.info("Getting result workflow : {} ", workflow);

        ParsedWorkflow parsedWorkflow = AppUtils.parseJSON(workflow.getWorkflowJson(), ParsedWorkflow.class);
        parsedWorkflow.setVersion(workflow.getVersion());
        parsedWorkflow.setId(workflow.getId());

        logger.info("Getting parsed parsedWorkflow : {} ", parsedWorkflow);

        return parsedWorkflow;
    }

}
