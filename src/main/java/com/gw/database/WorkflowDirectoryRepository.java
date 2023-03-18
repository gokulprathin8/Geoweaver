package com.gw.database;

import com.gw.jpa.WorkflowDirectory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkflowDirectoryRepository extends CrudRepository<com.gw.jpa.WorkflowDirectory, Long> {

    @Query(value = "select * from workflow_directory where gw_workspace_path= ?1 order by id desc limit 1", nativeQuery = true)
    Optional<WorkflowDirectory> getRecentWorkFlowPath(String workflowName);


}
