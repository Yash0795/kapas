package com.kapas.procurement.controller;

import com.kapas.procurement.model.Token;
import com.kapas.procurement.service.ProcurementService;
import com.kapas.user.entity.User;
import com.kapas.util.Constants;
import com.kapas.workorder.model.ParsedWorkflow;
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
public class ProcurementController {

    private static final Logger logger = LoggerFactory.getLogger(ProcurementController.class);

    private final ProcurementService procurementService;

    public ProcurementController(ProcurementService procurementService) {
        this.procurementService = procurementService;
    }

    @PostMapping(value = "/generateToken")
    public ResponseEntity<Token> generateToken(String workflowId,
                                               @RequestBody Map<String, Object> metaData,
                                               HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting workflowId : {}, metaData : {}, current User Id: {}", workflowId, metaData, currentUser.getId());
        Token token = procurementService.generateToken(workflowId, metaData, currentUser);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
