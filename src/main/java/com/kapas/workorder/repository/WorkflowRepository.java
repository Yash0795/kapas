package com.kapas.workorder.repository;

import com.kapas.workorder.entity.Workflow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends CrudRepository<Workflow, Integer> {

    @Query("select wf from Workflow wf where wf.workflowId = :workflowId and wf.version = (select max(wf2.version) " +
            "from Workflow wf2 where wf2.workflowId = :workflowId)")
    Workflow getLatestWorkflowByWorkflowId(@Param("workflowId") String workflowId);

}