package com.fintech.aiagent.infrastructure.ai;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class SimpleAIService implements AIService {
    private final Random random = new Random();

    @Override
    public double analyzeSentiment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        
        // 简单的情感分析实现：检查是否包含正面或负面词汇
        if (text.contains("谢谢") || text.contains("好") || text.contains("满意") && !text.contains("不满意")) {
            return 0.8;
        } else if (text.contains("不") || text.contains("差") || text.contains("慢") || text.contains("不满意")) {
            return -0.6;
        }
        return 0.0;
    }

    @Override
    public String detectIntent(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "GENERAL_QUERY";
        }
        
        // 简单的意图识别实现
        if (text.contains("查询") || text.contains("进度")) {
            return "QUERY_STATUS";
        } else if (text.contains("申请") || text.contains("贷款")) {
            return "APPLY_LOAN";
        } else if (text.contains("转人工") || text.contains("人工")) {
            return "TRANSFER_TO_HUMAN";
        }
        return "GENERAL_QUERY";
    }

    @Override
    public String generateResponse(String query, String currentIntent) {
        if (query == null) query = "";
        if (currentIntent == null) currentIntent = "GENERAL_QUERY";
        
        // 根据意图生成简单的回复
        switch (currentIntent) {
            case "QUERY_STATUS":
                return "您的申请正在处理中，预计还需要1-2个工作日完成审核。";
            case "APPLY_LOAN":
                return "好的，我来帮您办理贷款申请。请问您需要申请多少金额？";
            case "TRANSFER_TO_HUMAN":
                return "正在为您转接人工客服，请稍候...";
            default:
                return "抱歉，我可能没有完全理解您的问题。您可以换个方式描述，或者选择转接人工客服。";
        }
    }

    @Override
    public String performASR(String audioUrl) {
        if (audioUrl == null || audioUrl.trim().isEmpty()) {
            return "无效的音频输入";
        }
        // 模拟语音识别
        return "这是一个模拟的语音识别结果，实际项目中需要对接真实的ASR服务。";
    }

    @Override
    public String performTTS(String text) {
        if (text == null || text.trim().isEmpty()) {
            text = "空文本输入";
        }
        // 模拟语音合成，返回一个假的音频URL
        return "http://example.com/tts/" + System.currentTimeMillis() + ".mp3";
    }
} 