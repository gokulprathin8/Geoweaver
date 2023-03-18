package com.gw.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowDirectoryRepository extends CrudRepository<com.gw.jpa.WorkflowDirectory, Long> {

}
