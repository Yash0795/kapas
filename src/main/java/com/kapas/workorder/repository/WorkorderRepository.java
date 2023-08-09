package com.kapas.workorder.repository;

import com.kapas.workorder.entity.Workflow;
import com.kapas.workorder.entity.Workorder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
public interface WorkorderRepository extends CrudRepository<Workorder, Integer> {

    Workorder getWorkorderByWorkorderId(String workorderId);

    @Query("select count(*) as cnt from Workorder wo where date(wo.creationTime) = CURRENT_DATE() ")
    Integer getCurrentDatesWorkorderCount();


}