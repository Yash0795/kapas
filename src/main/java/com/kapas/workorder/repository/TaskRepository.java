package com.kapas.workorder.repository;

import com.kapas.user.entity.Role;
import com.kapas.workorder.entity.Task;
import com.kapas.workorder.entity.Workorder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

    Task getTaskByTaskId(String taskId);

    List<Task> getTasksByRole(Role role);
}