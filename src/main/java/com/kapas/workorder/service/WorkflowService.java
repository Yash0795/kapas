package com.kapas.workorder.service;

import com.kapas.util.AppUtils;
import com.kapas.workorder.entity.Workflow;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);

    private final WorkflowRepository workflowRepository;

    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public ParsedWorkflow loadWorkflow(String workflowId){

        Workflow workflow =
                workflowRepository.getLatestWorkflowByWorkflowId(workflowId);
        logger.debug("Getting result workflow : {} ", workflow);

        ParsedWorkflow parsedWorkflow = AppUtils.parseJSON(workflow.getWorkflowJson(), ParsedWorkflow.class);
        parsedWorkflow.setVersion(workflow.getVersion());
        parsedWorkflow.setId(workflow.getId());
        parsedWorkflow.setCreatedBy(workflow.getCreatedBy());
        parsedWorkflow.setModifiedBy(workflow.getModifiedBy());
        parsedWorkflow.setCreationTime(workflow.getCreationTime());
        parsedWorkflow.setModificationTime(workflow.getModificationTime());
        logger.debug("Getting parsed parsedWorkflow : {} ", parsedWorkflow);

        return parsedWorkflow;
    }

}
