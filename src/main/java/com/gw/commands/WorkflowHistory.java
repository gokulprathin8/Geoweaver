package com.gw.commands;

import com.gw.tools.HistoryTool;
import com.gw.tools.WorkflowTool;
import com.gw.utils.BaseTool;
import com.gw.utils.BeanTool;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "workflow-history", description = "Get history of a workflow")
public class WorkflowHistory implements Runnable {

    @CommandLine.Parameters(index = "0", description = "workflow id")
    String workflowId;

    @Override
    public void run() {
        String resp;
        WorkflowTool wt = BeanTool.getBean(WorkflowTool.class);
        HistoryTool tool = BeanTool.getBean(HistoryTool.class);
        if(BaseTool.isNull(workflowId)) {
            resp = wt.all_active_process();
        }else {
            resp = tool.workflow_all_history(workflowId);
        }
        System.out.println(resp);
    }
}
