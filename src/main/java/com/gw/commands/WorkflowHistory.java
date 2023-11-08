package com.gw.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.tools.HistoryTool;
import com.gw.tools.WorkflowTool;
import com.gw.utils.BaseTool;
import com.gw.utils.BeanTool;
import com.gw.utils.CommandLineUtil;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import static com.gw.utils.CommandLineUtil.CommandLineTable.getTextSafe;

@Component
@CommandLine.Command(name = "workflow-history", description = "Get history of a workflow")
public class WorkflowHistory implements Runnable {

    @CommandLine.Parameters(index = "0", description = "workflow id")
    String workflowId;

    @Override
    public void run() {
        CommandLineUtil.CommandLineTable table = new CommandLineUtil.CommandLineTable();
        table.setHeaders(new String[]{"History Id", "History Input", "History Output",
                "History Begin Time", "History End Time", "History Notes", "History Process",
                "Host Id", "Indicator"
        });
        String resp;

        WorkflowTool wt = BeanTool.getBean(WorkflowTool.class);
        HistoryTool tool = BeanTool.getBean(HistoryTool.class);
        if (BaseTool.isNull(workflowId)) {
            resp = wt.all_active_process();
        } else {
            resp = tool.workflow_all_history(workflowId);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(resp);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    table.addRow(
                            getTextSafe(node, "history_id"),
                            getTextSafe(node, "history_input"),
                            getTextSafe(node, "history_output"),
                            getTextSafe(node, "history_begin_time"),
                            getTextSafe(node, "history_end_time"),
                            getTextSafe(node, "history_notes"),
                            getTextSafe(node, "history_process"),
                            getTextSafe(node, "host_id"),
                            getTextSafe(node, "indicator")
                    );
                }
            }
            table.print();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}