package com.feng.agent.tools;

import com.alibaba.cloud.ai.graph.node.LlmNode;
import java.util.List;
import java.util.Map;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @since 2025/5/13
 */
@Service
public class FinancialReportService {

  @Tool(description = "get the financial report by stock codes")
  public Map<String,String> getFinancialReport(@ToolParam(description = "Stock Codes") List<String> stockCodes){
    return Map.of();
  }

  @Tool(description = "analysis stocks by stock codes")
  public Map<String,String> analyzeStocks(@ToolParam(description = "Stock Codes") List<String> stockCodes){
    return Map.of();
  }


}
