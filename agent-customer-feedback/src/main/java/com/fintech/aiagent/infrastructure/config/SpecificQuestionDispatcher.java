package com.fintech.aiagent.infrastructure.config;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.fintech.aiagent.interfaces.rest.CustomerServiceController;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @since 2025/4/25
 */
public class SpecificQuestionDispatcher implements EdgeAction {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackQuestionDispatcher.class);

    @Override
    public String apply(OverAllState state) throws Exception {

        String classifierOutput = (String)state.value("classifier_output").orElse("");
        logger.info("classifierOutput: " + classifierOutput);

        Map<String, String> classifierMap = new HashMap<>();
        classifierMap.put("after-sale", "after-sale");
        classifierMap.put("quality", "quality");
        classifierMap.put("transportation", "transportation");

        for (Map.Entry<String, String> entry : classifierMap.entrySet()) {
            if (classifierOutput.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "others";
    }
}
