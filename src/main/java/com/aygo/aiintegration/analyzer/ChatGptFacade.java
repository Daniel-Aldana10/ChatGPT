package com.aygo.aiintegration.analyzer;

import com.aygo.aiintegration.ChatRequest;
import com.aygo.aiintegration.adapter.ChatGptAdapterProxy;

/**
 * Facade for the ChatGPT flow.
 * Orchestrates input processing, AI call, and error handling.
 */
public class ChatGptFacade {
    private final ValidateInputProcessor chain;
    private final ChatGptAdapterProxy proxy;

    /**
     * Constructs a new ChatGptFacade.
     * @param chain The input processing chain (starting with ValidateInputProcessor)
     * @param proxy The proxy adapter for AI calls
     */
    public ChatGptFacade(ValidateInputProcessor chain, ChatGptAdapterProxy proxy) {
        this.chain = chain;
        this.proxy = proxy;
    }

    /**
     * Processes the input, calls the AI, and handles errors.
     * @param input The user input wrapped in a ChatRequest
     * @return The AI's response or an error message
     */
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