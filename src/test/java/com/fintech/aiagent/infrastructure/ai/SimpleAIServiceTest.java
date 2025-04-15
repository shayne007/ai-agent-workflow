package com.fintech.aiagent.infrastructure.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SimpleAIService Tests")
class SimpleAIServiceTest {

    private SimpleAIService aiService;

    @BeforeEach
    void setUp() {
        aiService = new SimpleAIService();
    }

    @DisplayName("情感分析测试 - 正面情感")
    @ParameterizedTest
    @ValueSource(strings = {
        "非常好的服务",
        "谢谢你的帮助",
        "我很满意"
    })
    void shouldDetectPositiveSentiment(String text) {
        double sentiment = aiService.analyzeSentiment(text);
        assertTrue(sentiment > 0, "应该检测到正面情感");
    }

    @DisplayName("情感分析测试 - 负面情感")
    @ParameterizedTest
    @ValueSource(strings = {
        "服务太差了",
        "速度太慢了",
        "不满意这个结果"
    })
    void shouldDetectNegativeSentiment(String text) {
        double sentiment = aiService.analyzeSentiment(text);
        assertTrue(sentiment < 0, "应该检测到负面情感");
    }

    @DisplayName("情感分析测试 - 中性情感")
    @Test
    void shouldDetectNeutralSentiment() {
        double sentiment = aiService.analyzeSentiment("请问贷款怎么申请");
        assertEquals(0.0, sentiment, "应该检测到中性情感");
    }

    @DisplayName("意图识别测试")
    @ParameterizedTest
    @CsvSource({
        "我要查询贷款进度, QUERY_STATUS",
        "帮我申请一笔贷款, APPLY_LOAN",
        "请转接人工服务, TRANSFER_TO_HUMAN",
        "今天天气真好, GENERAL_QUERY"
    })
    void shouldDetectCorrectIntent(String input, String expectedIntent) {
        String intent = aiService.detectIntent(input);
        assertEquals(expectedIntent, intent, "应该正确识别用户意图");
    }

    @DisplayName("回复生成测试")
    @Test
    void shouldGenerateAppropriateResponse() {
        // 测试贷款申请意图
        String loanResponse = aiService.generateResponse("我要申请贷款", "APPLY_LOAN");
        assertTrue(loanResponse.contains("申请") && loanResponse.contains("金额"),
            "贷款申请回复应包含关键信息");

        // 测试查询进度意图
        String queryResponse = aiService.generateResponse("查询进度", "QUERY_STATUS");
        assertTrue(queryResponse.contains("处理中") && queryResponse.contains("审核"),
            "进度查询回复应包含状态信息");

        // 测试转人工意图
        String transferResponse = aiService.generateResponse("转人工", "TRANSFER_TO_HUMAN");
        assertTrue(transferResponse.contains("转接") && transferResponse.contains("人工"),
            "转人工回复应包含转接相关信息");
    }

    @DisplayName("语音识别测试")
    @Test
    void shouldPerformASR() {
        String result = aiService.performASR("test-audio-url");
        assertNotNull(result, "语音识别结果不应为空");
        assertTrue(result.contains("语音识别"), "应返回模拟的语音识别结果");
    }

    @DisplayName("语音合成测试")
    @Test
    void shouldPerformTTS() {
        String audioUrl = aiService.performTTS("测试文本");
        assertNotNull(audioUrl, "语音合成URL不应为空");
        assertTrue(audioUrl.startsWith("http://") && audioUrl.endsWith(".mp3"),
            "应返回有效的音频URL格式");
    }

    @DisplayName("边界情况测试")
    @Test
    void shouldHandleEdgeCases() {
        // 测试空输入
        assertDoesNotThrow(() -> aiService.analyzeSentiment(""),
            "空字符串不应导致异常");
        assertDoesNotThrow(() -> aiService.detectIntent(""),
            "空字符串不应导致异常");
        assertDoesNotThrow(() -> aiService.generateResponse("", "GENERAL_QUERY"),
            "空字符串不应导致异常");

        // 测试null输入
        assertDoesNotThrow(() -> aiService.generateResponse(null, null),
            "null输入不应导致异常");
    }
} 