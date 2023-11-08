package com.gw.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.tools.ProcessTool;
import com.gw.utils.BaseTool;
import com.gw.utils.BeanTool;
import com.gw.utils.CommandLineUtil;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import static com.gw.utils.CommandLineUtil.CommandLineTable.getTextSafe;

@Component
@CommandLine.Command(name = "process-history", description = "Get history of a process")
public class ProcessHistoryCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "process id")
    String processId;

    @Override
    public void run() {
        CommandLineUtil.CommandLineTable table = new CommandLineUtil.CommandLineTable();
        table.setHeaders(new String[]{"History Id", "History Input", "History Output",
                "History Begin Time", "History End Time", "History Notes", "History Process",
                "Host Id", "Indicator"
        });
        String resp;

        ProcessTool pt = BeanTool.getBean(ProcessTool.class);
        if (BaseTool.isNull(processId)) {
            resp = pt.all_active_process();
        } else {
            resp = pt.all_history(processId);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(resp);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    table.addRow(
                            getTextSafe(node, "history_id"),
                            getTextSafe(node, "history_input"),
                            getTextSafe(node, "history_output"), // Replaced 'history_begin_time' with 'history_output'
                            getTextSafe(node, "history_begin_time"),
                            getTextSafe(node, "history_end_time"),
                            getTextSafe(node, "history_notes"), // Replaced 'history_end' with 'history_notes'
                            getTextSafe(node, "history_process"),
                            getTextSafe(node, "host_id"),
                            getTextSafe(node, "indicator")
                    );
                }
            }
            table.print();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
