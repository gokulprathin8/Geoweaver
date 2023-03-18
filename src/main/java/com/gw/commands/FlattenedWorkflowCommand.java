package com.gw.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.database.WorkflowDirectoryRepository;
import com.gw.jpa.WorkflowDirectory;
import com.gw.tools.WorkflowTool;
import com.gw.utils.BaseTool;
import com.gw.utils.BeanTool;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

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
            FileUtils.copyDirectory(sourceDirectory.toFile(), destinationDirectory.toFile());
            String resp = wt.precheck(workflowJSON, true);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(resp, Map.class);
            String wid = String.valueOf(map.get("id"));
            String workflowJSONFolder = workflowJSON + "."; // bypass substring check
            wt.saveWorkflowFromFolder(wid, String.valueOf(Paths.get(workflowJSONFolder).getFileName()));
            WorkflowDirectoryRepository wdr = BeanTool.getBean(WorkflowDirectoryRepository.class);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Do you wish to save your code changes from Geoweaver to your Code Directory?  [Y/N]:  ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("yes")) {
                WorkflowDirectory workflowDirectory = new WorkflowDirectory();
                workflowDirectory.setGwWorkspacePath(String.valueOf(Paths.get(workflowJSON).getFileName()));
                workflowDirectory.setSourcePath(String.valueOf(Paths.get(workflowJSON).getParent().toAbsolutePath()));
                wdr.save(workflowDirectory);
            }
            System.out.println("Export completed.");
        } catch (Exception e) {
            System.out.printf(e.toString());
        }
    }
}
