package com.kapas.workorder.controller;

import com.kapas.user.entity.User;
import com.kapas.util.Constants;
import com.kapas.workorder.entity.Task;
import com.kapas.workorder.service.TaskService;
import com.kapas.workorder.service.WorkorderService;
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
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping(value = "/createTask")
    public ResponseEntity<Task> createTask(String workorderId,
                                           String taskId,
                                           HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting workorderId : {}, taskId : {},  currentUserId: {}",
                workorderId, taskId, currentUser.getId());
        Task task = taskService.createTask(workorderId, taskId, currentUser);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping(value = "/startTask")
    public ResponseEntity<String> startTask(String taskId,
                                               String remark,
                                               @RequestBody Map<String, Object> taskData,
                                               HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting taskId : {}, remark : {}, taskData : {},  currentUserId: {}",
                taskId, remark, taskData, currentUser.getId());
        taskService.startTask(taskId, taskData, remark, currentUser);
        return new ResponseEntity<>("Task started.", HttpStatus.OK);
    }

    @PostMapping(value = "/completeTask")
    public ResponseEntity<String> completeTask(String taskId,
                                             @RequestBody Map<String, Object> taskData,
                                             HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting taskId : {}, taskData : {},  currentUserId: {}",
                taskId, taskData, currentUser.getId());
        taskService.completeTask(taskId, taskData, currentUser);
        return new ResponseEntity<>("Task completed.", HttpStatus.OK);
    }

    @GetMapping(value = "/closeTask")
    public ResponseEntity<String> closeTask(String taskId,
                                            String remark,
                                            HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting taskId : {}, remark : {}, currentUserId: {}",
                taskId, remark, currentUser.getId());
        taskService.closeTask(taskId, remark, currentUser);
        return new ResponseEntity<>("Task closed.", HttpStatus.OK);
    }

/*    @GetMapping(value = "/tasks")
    public ResponseEntity<String> tasks(HttpServletRequest request) throws Exception {
        User currentUser = (User) request.getAttribute(Constants.PRINCIPAL);
        logger.info("Getting currentUserId: {}", currentUser.getId());
        taskService.getCurrentUserTasks(currentUser);
        return new ResponseEntity<>("Task closed.", HttpStatus.OK);
    }*/

}
