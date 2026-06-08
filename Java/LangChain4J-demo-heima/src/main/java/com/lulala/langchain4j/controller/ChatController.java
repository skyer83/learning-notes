package com.lulala.langchain4j.controller;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenjh
 * @version 1.0
 * @since 2026/6/8 21:34
 */
@RestController
@RequestMapping("/openai")
public class ChatController {

    @Autowired
    OpenAiChatModel openAiChatModel;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message") String message) {
        return openAiChatModel.chat(message);
    }
}
