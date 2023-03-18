package com.gw.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "workflow", description = "Run a workflow in geoweaver directly from directory")
public class FlattenedWorkflowCommand implements Runnable{

    @CommandLine.Option(names = {"-p", "--workflow-json-path"}, description = "path to geoweaver workflow json")
    String workflowJSON;

    @Override
    public void run() {
        try {



        } catch (Exception e) {
            System.out.printf(e.toString());
        }
    }
}
