/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fintech.aiagent.infrastructure.config;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.GraphRepresentation.Type;
import com.alibaba.cloud.ai.graph.GraphStateException;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.node.QuestionClassifierNode;
import com.alibaba.cloud.ai.graph.state.AgentStateFactory;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowAutoconfiguration {

  private static final String SPECIFIC_QUESTION_CLASSIFIER = "specific_question_classifier";
  private static final String FEEDBACK_CLASSIFIER = "feedback_classifier";
  private static final String RECORDER = "recorder";

  @Bean
  public StateGraph workflowGraph(ChatModel chatModel) throws GraphStateException {

    ChatClient chatClient = ChatClient.builder(chatModel).defaultAdvisors(new SimpleLoggerAdvisor())
        .build();

    AgentStateFactory<OverAllState> stateFactory = (inputs) -> {
      OverAllState state = new OverAllState();
      state.registerKeyAndStrategy("input", new ReplaceStrategy());
      state.registerKeyAndStrategy("classifier_output", new ReplaceStrategy());
      state.registerKeyAndStrategy("solution", new ReplaceStrategy());
      state.input(inputs);
      return state;
    };

    StateGraph stateGraph =
        new StateGraph("Consumer Service Workflow Demo", stateFactory)
            .addNode(FEEDBACK_CLASSIFIER, node_async(buildFeelingClassifierNode(chatClient)))
            .addNode(SPECIFIC_QUESTION_CLASSIFIER, node_async(buildQuestionClassifierNode(
                chatClient)))
            .addNode(RECORDER, node_async(new RecordingNode()))

            .addEdge(START, FEEDBACK_CLASSIFIER)
            // according to FeedbackQuestionDispatcher logic, if the user is giving positive feedback,
            // then go to recorder node, otherwise, go to specific question classifier node
            .addConditionalEdges(FEEDBACK_CLASSIFIER, edge_async(new FeedbackQuestionDispatcher()),
                Map.of("positive", RECORDER, "negative", SPECIFIC_QUESTION_CLASSIFIER))
            // according to SpecificQuestionDispatcher logic, go to specific question classifier node
            // RECORDER node is added to handle all other cases
            .addConditionalEdges(SPECIFIC_QUESTION_CLASSIFIER,
                edge_async(new SpecificQuestionDispatcher()),
                Map.of("after-sale", RECORDER, "transportation", RECORDER, "quality",
                    RECORDER, "others", RECORDER))
            .addEdge(RECORDER, END);

    GraphRepresentation graphRepresentation = stateGraph.getGraph(Type.MERMAID, "workflow graph");

    System.out.println("\n\n");
    System.out.println(graphRepresentation.content());
    System.out.println("\n\n");

    return stateGraph;
  }

  private QuestionClassifierNode buildQuestionClassifierNode(ChatClient chatClient) {
    return QuestionClassifierNode.builder().chatClient(chatClient)
        .inputTextKey("input")
        .categories(
            List.of("after-sale service", "transportation", "product quality", "others"))
        .classificationInstructions(List.of(
            "What kind of service or help the customer is trying to get from us? Classify the question based on your understanding."))
        .build();
  }

  private QuestionClassifierNode buildFeelingClassifierNode(ChatClient chatClient) {
    return QuestionClassifierNode.builder().chatClient(chatClient)
        .inputTextKey("input")
        .categories(List.of("positive feedback", "negative feedback"))
        .classificationInstructions(
            List.of("Try to understand the user's feeling when he/she is giving the feedback."))
        .build();
  }

}
