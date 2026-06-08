package com.lulala.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * LangChain4j_会话功能_快速入门
 * @author shenjh
 * @version 1.0
 * @since 2026/6/8 21:01
 */
public class SimpleApplication {

    public static void main(String[] args) {
        String apiKey = System.getenv("ALI_YUNBAILIAN_API_KEY");
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(apiKey)
                .modelName("qwen3.7-plus")
                .logRequests(true)
                .logResponses(true)
                .build();
        String response = chatModel.chat("你是谁？");
        System.out.println(response);
    }

}
