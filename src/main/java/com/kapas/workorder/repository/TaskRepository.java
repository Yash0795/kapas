package com.kapas.workorder.repository;

import com.kapas.workorder.entity.Task;
import com.kapas.workorder.entity.Workorder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {


}