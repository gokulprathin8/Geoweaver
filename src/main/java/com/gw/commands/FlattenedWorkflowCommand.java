package com.gw.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.tools.WorkflowTool;
import com.gw.utils.BaseTool;
import com.gw.utils.BeanTool;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
@CommandLine.Command(name = "workflow-directory", description = "Run a workflow in geoweaver directly from directory")
public class FlattenedWorkflowCommand implements Runnable{

//    @CommandLine.Option(names = {"-p", "--workflow-json-path"}, description = "path to geoweaver workflow json")
//    String workflowJSON;

    @CommandLine.Parameters(index = "0", description = "Path to geoweaver workflow.json")
    String workflowJSON;

    @Override
    public void run() {
        try {
            WorkflowTool wt = BeanTool.getBean(WorkflowTool.class);
            BaseTool bt = BeanTool.getBean(BaseTool.class);
            Path sourceDirectory = Paths.get(workflowJSON).toAbsolutePath();
            Path destinationDirectory = Paths.get(bt.getFileTransferFolder() + Paths.get(workflowJSON).getFileName());
            System.out.println(sourceDirectory);
            System.out.println(destinationDirectory);
            FileUtils.copyDirectory(sourceDirectory.toFile(), destinationDirectory.toFile());
            String resp = wt.precheck(workflowJSON, true);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(resp, Map.class);
            String wid = String.valueOf(map.get("id"));
            workflowJSON = workflowJSON + "."; // bypass substring check
            wt.saveWorkflowFromFolder(wid, workflowJSON);
            System.out.println("here");

        } catch (Exception e) {
            System.out.printf(e.toString());
        }
    }
}
