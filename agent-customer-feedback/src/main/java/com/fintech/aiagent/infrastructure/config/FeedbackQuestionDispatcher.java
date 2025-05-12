package com.fintech.aiagent.infrastructure.config;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.fintech.aiagent.interfaces.rest.CustomerServiceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @since 2025/4/25
 */
public class FeedbackQuestionDispatcher implements EdgeAction {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackQuestionDispatcher.class);

    @Override
    public String apply(OverAllState state) throws Exception {

        String classifierOutput = (String)state.value("classifier_output").orElse("");
        logger.info("classifierOutput: " + classifierOutput);

        if (classifierOutput.contains("positive")) {
            return "positive";
        }
        return "negative";
    }
}
