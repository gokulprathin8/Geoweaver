package com.gw.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.database.WorkflowDirectoryRepository;
import com.gw.jpa.History;
import com.gw.jpa.WorkflowDirectory;
import com.gw.tools.HistoryTool;
import com.gw.tools.WorkflowTool;
import com.gw.utils.BaseTool;
import com.gw.utils.BeanTool;
import com.gw.utils.RandomString;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@CommandLine.Command(name = "workflow-directory", description = "Run a workflow in geoweaver directly from directory")
public class FlattenedWorkflowCommand implements Runnable{
//    private ApplicationContext applicationContext;
//    @CommandLine.Option(names = {"-p", "--workflow-json-path"}, description = "path to geoweaver workflow json")
//    String workflowJSON;
    @CommandLine.Parameters(index = "0", description = "Path to geoweaver workflow.json")
    String workflowJSON;
    @CommandLine.Option(names = { "-e", "--environments" }, description = "environments to run on")
    String[] envs;
    @CommandLine.Option(names = { "-p", "--passwords" }, description = "passwords to the target hosts")
    String[] passes;
    @CommandLine.Option(names = { "-h", "--hosts" }, description = "hosts to run on")
    String[] hostStrings;

    @Override
    public void run() {
        try {
            WorkflowTool wt = BeanTool.getBean(WorkflowTool.class);
            HistoryTool ht = BeanTool.getBean(HistoryTool.class);
            BaseTool bt = BeanTool.getBean(BaseTool.class);
            Path sourceDirectory = Paths.get(workflowJSON).toAbsolutePath();
            Path destinationDirectory = Paths.get(bt.getFileTransferFolder() + Paths.get(workflowJSON).getFileName());
            FileUtils.copyDirectory(sourceDirectory.toFile(), destinationDirectory.toFile());
            String resp = wt.precheck(workflowJSON, true);
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(resp, Map.class);
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
            String historyId = new RandomString(18).nextString();
            if(BaseTool.isNull(envs)) envs = new String[]{"default_option"};

            if(BaseTool.isNull(hostStrings)) hostStrings = new String[]{"10001"};

            String history = wt.execute(historyId, String.valueOf(Paths.get(workflowJSON).getFileName()), "one", hostStrings,
                    passes, envs, "xxxxxxxxxx");
            System.out.println(history);
            List<Map<java.lang.constant.Constable, java.io.Serializable>> list = new ArrayList<>();
            History hist;
            try {

                while(true){

                    TimeUnit.SECONDS.sleep(2);

                    hist = ht.getHistoryById(historyId);
                    Map<java.lang.constant.Constable, java.io.Serializable> mMap = new HashMap<java.lang.constant.Constable, java.io.Serializable>();
                    mMap.put("history_id", hist.getHistory_id());
                    mMap.put("history_input", hist.getHistory_input());
                    mMap.put("history_output", hist.getHistory_output());
                    mMap.put("history_begin_time", hist.getHistory_begin_time().getTime() / 1000);
                    mMap.put("history_end_time", hist.getHistory_end_time().getTime() / 1000);
                    mMap.put("history_notes", hist.getHistory_notes());
                    mMap.put("history_process", hist.getHistory_process());
                    mMap.put("host_id", hist.getHost_id());
                    mMap.put("indicator", hist.getIndicator());
                    list.add(mMap);
                    if(ht.checkIfEnd(hist)) break;

                }

            } catch (InterruptedException e) {

                e.printStackTrace();

            }
            System.out.println(list);
            RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
            String historyFileName = generator + ".json";
            System.out.println("Export completed.");
        } catch (Exception e) {
            System.out.printf(e.toString());
        }
    }
}
