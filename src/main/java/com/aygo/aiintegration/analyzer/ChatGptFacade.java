package com.aygo.aiintegration.analyzer;

import com.aygo.aiintegration.adapter.ChatGptAdapterProxy;
import com.aygo.aiintegration.adapter.IAiAdapter;

public class ChatGptFacade {
    private final PreProcessor chain;
    private final ChatGptAdapterProxy proxy;

    public ChatGptFacade(PreProcessor chain, ChatGptAdapterProxy proxy) {
        this.chain = chain;
        this.proxy = proxy;
    }

    public String getResponse(String input) {
        try {
            String processed = chain.process(input);
            return proxy.generateResponse(processed);
        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage();
        }
    }
} 