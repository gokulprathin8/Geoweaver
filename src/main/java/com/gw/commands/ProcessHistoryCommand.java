package com.gw.commands;

import com.gw.tools.ProcessTool;
import com.gw.utils.BaseTool;
import com.gw.utils.BeanTool;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "process-history", description = "Get history of a process")
public class ProcessHistoryCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "process id")
    String processId;

    @Override
    public void run() {
        String resp;
        ProcessTool pt = BeanTool.getBean(ProcessTool.class);
        if (BaseTool.isNull(processId)) {
            resp = pt.all_active_process();
        } else {
            resp = pt.all_history(processId);
        }
        System.out.print(resp);
    }
}
