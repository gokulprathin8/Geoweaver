package com.gw.commands;

import com.gw.tools.ProcessTool;
import com.gw.utils.BaseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "process-history", description = "Get history of a process")
public class ProcessHistoryCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "process id")
    String processId;

    @Autowired
    ProcessTool pt;

    @Override
    public void run() {
        if (BaseTool.isNull(processId)) {
            String resp = pt.all_active_process();
            System.out.print(resp);
        }
    }
}
