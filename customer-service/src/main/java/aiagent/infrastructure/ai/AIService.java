package aiagent.infrastructure.ai;

public interface AIService {
    /**
     * 分析文本的情感倾向
     * @param text 输入文本
     * @return 情感得分 (-1.0 到 1.0)
     */
    double analyzeSentiment(String text);

    /**
     * 检测用户意图
     * @param text 输入文本
     * @return 意图标识
     */
    String detectIntent(String text);

    /**
     * 生成回复内容
     * @param query 用户查询
     * @param currentIntent 当前意图
     * @return 生成的回复
     */
    String generateResponse(String query, String currentIntent);

    /**
     * 语音识别 (ASR)
     * @param audioUrl 音频文件URL
     * @return 识别的文本
     */
    String performASR(String audioUrl);

    /**
     * 语音合成 (TTS)
     * @param text 要转换的文本
     * @return 合成的音频URL
     */
    String performTTS(String text);
} 