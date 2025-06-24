package com.aygo.aiintegration.service;

import com.aygo.aiintegration.ChatRequest;
import com.aygo.aiintegration.adapter.ChatGptAdapter;
import com.aygo.aiintegration.adapter.ChatGptAdapterProxy;
import com.aygo.aiintegration.analyzer.*;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatGptAdapterProxy proxy;

    public AiService(ChatGptAdapter chatGptAdapter) {
        this.proxy = new ChatGptAdapterProxy(chatGptAdapter);
    }

    public String generateResponse(ChatRequest input) {
        PreProcessor validate = new ValidateInputProcessor(5);
        PreProcessor clean = new CleanInputProcessor();
        PreProcessor improve = new ImproveInputProcessor(false);
        validate.setNext(clean);
        clean.setNext(improve);

        ChatGptFacade facade = new ChatGptFacade(validate, proxy);
        return facade.getResponse(input);
    }
}
