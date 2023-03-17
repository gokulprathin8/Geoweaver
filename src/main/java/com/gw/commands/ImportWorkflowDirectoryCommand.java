package com.gw.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.database.HistoryRepository;
import com.gw.jpa.History;
import com.gw.jpa.Workflow;
import com.gw.tools.ProcessTool;
import com.gw.tools.WorkflowTool;
import com.gw.utils.BaseTool;
import com.gw.utils.BeanTool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.*;
import java.util.Map;

@Component
@CommandLine.Command(name = "directory", description = "import a unzipped directory into geoweaver")
public class ImportWorkflowDirectoryCommand implements Runnable {

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    ProcessTool processTool;

    private final String WORKFLOW_FILE = "/workflow.json";
    private final String PROCESS_FILE = "/process.json";
    private final String CODE_DIRECTORY = "/code";
    private final String HISTORY_DIRECTORY = "/history";

    @CommandLine.Parameters(index = "0", description = "Geoweaver unzipped workflow directory path")
    String workflowPath;

    public void checkValidDirectory() throws FileNotFoundException {
        // check if workflow.json exists
        if (!new File(workflowPath + WORKFLOW_FILE).exists()) {
            throw new FileNotFoundException("workflow.json file does not exist");
        } else if (!new File(workflowPath + CODE_DIRECTORY).exists()) {
            throw new FileNotFoundException("code directory does not exist");
        } else if (!new File(workflowPath + HISTORY_DIRECTORY).exists()) {
            throw new FileNotFoundException("history directory does not exist");
        }
    }



    public void run() {

        try {
            JSONParser parser = new JSONParser();
            WorkflowTool wt = BeanTool.getBean(WorkflowTool.class);
            BaseTool bt = BeanTool.getBean(BaseTool.class);

            Object obj = parser.parse(new FileReader(workflowPath + WORKFLOW_FILE));
            JSONObject jsonObject = (JSONObject) obj;

            Path workflowDirectory = Paths.get(workflowPath);
            Path workflowDirectoryName = workflowDirectory.getFileName();

            // check if all files and directories exist
            checkValidDirectory();
            System.out.println("Found valid geoweaver import directory");

            // copy all files to workspace
            Files.copy(workflowDirectory, new File(
                    bt.getFileTransferFolder() + workflowDirectoryName
            ).toPath(), StandardCopyOption.REPLACE_EXISTING);

            // save workflow
            Workflow workflowJSON = wt.fromJSON(String.valueOf(jsonObject));
            wt.save(workflowJSON);

            // save history
            String historyFolder = workflowPath + "history" + FileSystems.getDefault().getSeparator();
            File[] historyFiles = new File(historyFolder).listFiles();
            if (historyFiles != null) {
                for (File historyFile : historyFiles) {
                    String historyJSON = bt.readStringFromFile(historyFile.getAbsolutePath());
                    JSONArray historyArray = (JSONArray) parser.parse(historyJSON);
                    for (Object o : historyArray) {
                        String object = ((JSONObject) o).toJSONString();
                        ObjectMapper mapper = new ObjectMapper();
                        History history = mapper.readValue(object, History.class);
                        historyRepository.save(history);
                    }
                }
            }

//            String workflowJSON = bt.getWorkflowJsonPath(workflowPath);
//            String workflowFolderPath = workflowJSON.substring(0,
//                    workflowJSON.lastIndexOf("workflow.json"));
//            String codeFolder = workflowFolderPath + "code";
//            String historyFolder = workflowFolderPath + "history";
//
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, String> map = mapper.readValue(workflowJSON, Map.class);
//            String wid = String.valueOf(map.get("id"));
//
//            wt.saveWorkflowFromFolder(wid, String.valueOf(workflowDirectoryName));



//            // save workflow
//            Workflow workflowJSON = wt.fromJSON(String.valueOf(jsonObject));
//            wt.save(workflowJSON);
//
//            // save history
//            String historyFolder = workflowPath + "history" + FileSystems.getDefault().getSeparator();
//            File[] historyFiles = new File(historyFolder).listFiles();
//            if (historyFiles != null) {
//                for (File historyFile : historyFiles) {
//                    String historyJSON = bt.readStringFromFile(historyFile.getAbsolutePath());
//                    JSONArray historyArray = (JSONArray) parser.parse(historyJSON);
//                    for (Object o : historyArray) {
//                        String object = ((JSONObject) o).toJSONString();
//                        ObjectMapper mapper = new ObjectMapper();
//                        History history = mapper.readValue(object, History.class);
//                        historyRepository.save(history);
//                    }
//                }
//            }
//
//            // save process
//            Path processFilePath = Paths.get(workflowPath + PROCESS_FILE);
//            if (Files.exists(processFilePath)) {
//                Object processObject = parser.parse(new FileReader(workflowPath + PROCESS_FILE));
//                GWProcess process = processTool.fromJSON((String) processObject);
//                processTool.save(process);
//            }


        } catch (Exception e) {
            System.err.println(e.toString() + " | " + "Directory path: " + workflowPath.trim());


        }

    }
}
