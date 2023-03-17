package com.gw.database;

import com.gw.jpa.WorkflowDirectory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowDirectoryRepository extends JpaRepository<WorkflowDirectory, String> {

}
