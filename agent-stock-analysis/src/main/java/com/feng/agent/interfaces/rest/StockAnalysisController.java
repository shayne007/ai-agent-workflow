/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feng.agent.interfaces.rest;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;
import static com.feng.agent.openmanus.OpenManusPrompt.STEP_SYSTEM_PROMPT;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.GraphRepresentation.Type;
import com.alibaba.cloud.ai.graph.GraphStateException;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.node.LlmNode;
import com.alibaba.cloud.ai.graph.node.ToolNode;
import com.alibaba.cloud.ai.graph.state.AgentStateFactory;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.feng.agent.config.ShouldContinueDispatcher;
import com.feng.agent.openmanus.SupervisorAgent;
import com.feng.agent.openmanus.tool.Builder;
import com.feng.agent.openmanus.tool.PlanningTool;
import com.feng.agent.prompts.StockAnalysisPrompt;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock-analysis")
public class StockAnalysisController {

  private static final String PLAN_NODE_ID = "plan_node";
  private static final String LLM_CALL_NODE_ID = "llm_call";
  private static final String ENVIRONMENT_TOOL_NODE_ID = "environment";
  private final ChatClient planningClient;

  private final ChatClient stepClient;

  private CompiledGraph compiledGraph;

  // 也可以使用如下的方式注入 ChatClient
  public StockAnalysisController(ChatModel chatModel) throws GraphStateException {

    this.planningClient = ChatClient.builder(chatModel)
        .defaultAdvisors(new SimpleLoggerAdvisor())
        .defaultOptions(OpenAiChatOptions.builder().internalToolExecutionEnabled(false).build())
        .build();

    this.stepClient = ChatClient.builder(chatModel)
        .defaultTools(List.of("getFinancialReport", "analyzeStocks"))
        .defaultOptions(OpenAiChatOptions.builder().internalToolExecutionEnabled(false).build())
        .build();

    initGraph();
  }

  public void initGraph() throws GraphStateException {

    AgentStateFactory<OverAllState> stateFactory = (inputs) -> {
      OverAllState state = new OverAllState();
      state.registerKeyAndStrategy("input", new ReplaceStrategy());
      state.registerKeyAndStrategy("plan", new ReplaceStrategy());
      state.registerKeyAndStrategy("messages", new ReplaceStrategy());
      state.registerKeyAndStrategy("step_prompt", new ReplaceStrategy());
      state.registerKeyAndStrategy("step_output", new ReplaceStrategy());
      state.registerKeyAndStrategy("final_output", new ReplaceStrategy());
      state.input(inputs);
      return state;
    };

    // 1.use deepseek r1 to generate a plan
    LlmNode planningNode = LlmNode.builder().chatClient(planningClient)
        .systemPromptTemplate(StockAnalysisPrompt.PLANNING_PROMPT).userPromptTemplateKey("input")
        .outputKey("plan").messagesKey("messages").build();
    // 2. use deepseek v3 to decide whether to call a tool or not
    LlmNode llmCallNode = LlmNode.builder().chatClient(stepClient)
        .userPromptTemplate(StockAnalysisPrompt.STEP_PROMPT).paramsKey(List.of("plan", "messages"))
        .messagesKey("messages").build();
    // 3. call the tool
    ToolNode toolNode = ToolNode.builder().toolNames(List.of("getFinancialReport", "analyzeStocks"))
        .build();
    // create state graph
    StateGraph graph = new StateGraph(stateFactory)
        .addNode(PLAN_NODE_ID, node_async(planningNode))
        .addNode(LLM_CALL_NODE_ID, node_async(llmCallNode))
        .addNode(ENVIRONMENT_TOOL_NODE_ID, node_async(toolNode))

        .addEdge(START, PLAN_NODE_ID)
        .addEdge(PLAN_NODE_ID, LLM_CALL_NODE_ID)
        .addConditionalEdges(LLM_CALL_NODE_ID, edge_async(new ShouldContinueDispatcher()),
            Map.of("continue", ENVIRONMENT_TOOL_NODE_ID, "end", END))
        .addEdge(ENVIRONMENT_TOOL_NODE_ID, LLM_CALL_NODE_ID);

    this.compiledGraph = graph.compile();

    GraphRepresentation graphRepresentation = compiledGraph.getGraph(Type.MERMAID);
    System.out.println("stock analysis planning graph: \n\n");
    System.out.println(graphRepresentation.content());
    System.out.println("\n\n");
  }

  /**
   * ChatClient 简单调用
   */
  @GetMapping("/chat")
  public String simpleChat(String query) {

    return compiledGraph.invoke(Map.of("input", query, "plan", "")).get().data().toString();
  }

}
