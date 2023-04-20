package com.kapas.procurement.service;

import com.kapas.procurement.model.Token;
import com.kapas.user.entity.Role;
import com.kapas.user.entity.User;
import com.kapas.util.AppUtils;
import com.kapas.workorder.entity.Workflow;
import com.kapas.workorder.entity.Workorder;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.repository.TaskRepository;
import com.kapas.workorder.repository.WorkorderRepository;
import com.kapas.workorder.service.WorkflowService;
import com.kapas.workorder.service.WorkorderService;
import com.kapas.workorder.util.WorkorderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

@Service
public class ProcurementService {

    private static final Logger logger = LoggerFactory.getLogger(ProcurementService.class);

    private final WorkorderService workorderService;

    public ProcurementService(WorkorderService workorderService) {
        this.workorderService = workorderService;
    }

    public Token generateToken(String workflowId, Map<String, Object> metaData, User currentUser) throws NoSuchFieldException {
        Integer workorderCount = workorderService.getCurrentDatesWorkorderCount(Calendar.getInstance().getTime());
        logger.info("Getting current Workorder counts: " + workorderCount);

        String tokenId = AppUtils.leftPad(String.valueOf(workorderCount + 1), 3, "0");
        logger.info("Getting Token Id: " + tokenId);

        Workorder workorder = workorderService.createWorkorder(workflowId,
                tokenId + "_" + String.valueOf(currentUser.getId()), currentUser);

        workorderService.createTask(workorder.getWorkorderId(), "GENERATE_TOKEN", "GENERATE_TOKEN_ROLE",
                metaData, currentUser);

        return new Token(workorder.getWorkorderId(), tokenId);
    }
}
