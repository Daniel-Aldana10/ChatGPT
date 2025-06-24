package com.aygo.aiintegration.analyzer;

import com.aygo.aiintegration.ChatRequest;
import com.aygo.aiintegration.adapter.ChatGptAdapterProxy;

public class ChatGptFacade {
    private final PreProcessor chain;
    private final ChatGptAdapterProxy proxy;

    public ChatGptFacade(PreProcessor chain, ChatGptAdapterProxy proxy) {
        this.chain = chain;
        this.proxy = proxy;
    }

    public String getResponse(ChatRequest input) {
        try {
            String processed = chain.process(input.getInput());
            ChatRequest peticion = new ChatRequest();
            peticion.setInput(processed);
            return proxy.generateResponse(peticion);
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage();
        }
    }
} 