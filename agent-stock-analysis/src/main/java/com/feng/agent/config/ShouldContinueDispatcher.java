package com.feng.agent.config;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;

/**
 * TODO
 *
 * @since 2025/4/25
 */
public class ShouldContinueDispatcher implements EdgeAction {

  private static final Logger logger = LoggerFactory.getLogger(ShouldContinueDispatcher.class);

  @Override
  public String apply(OverAllState state) throws Exception {

    AssistantMessage message = (AssistantMessage) state.value("messages").get();
    String llmOutput = message.getText();
    logger.info("llm_output: " + llmOutput);

    if (llmOutput.contains("Final Answer")) {
      return "continue";
    }
    return "end";
  }
}
