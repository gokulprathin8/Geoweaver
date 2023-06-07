package com.gw.commands;

import com.gw.tools.WorkflowTool;
import com.gw.utils.BaseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "workflow-history", description = "Get history of a workflow")
public class WorkflowHistory implements Runnable {

    @CommandLine.Parameters(index = "0", description = "workflow id")
    String workflowId;

    @Autowired
    WorkflowTool wt;

    @Override
    public void run() {
        if(BaseTool.isNull(workflowId)) {
            String resp = wt.all_active_process();
            System.out.println(resp);

        }else {
            String resp = wt.all_history(workflowId);
            System.out.println(resp);
        }
    }
}
