package com.gw.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@CommandLine.Command(name = "workflow-import")
@Component
public class WorkflowCLIService {


    @CommandLine.Parameters(index = "0", description = "Path to workflow file")
    String workflowPath;

    private static final String workflowFileData = null;

    public void run() {

        File workflowFile = new File(workflowPath);
        try {
            Scanner readerObject = new Scanner(workflowFile);
            StringBuilder workflowJSONBuilder = new StringBuilder();
            while (readerObject.hasNextLine()) {
                String data = readerObject.nextLine();
                workflowJSONBuilder.append(data);
            }
            readerObject.close();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(workflowJSONBuilder.toString());
        } catch (FileNotFoundException e) {
            String errorString = String.format("An error occurred: %s", e.getMessage());
            System.out.println(errorString);
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
