package com.gw.database;

import com.gw.jpa.WorkflowDirectory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowDirectoryRepository extends CrudRepository<WorkflowDirectory, Long> {

}
