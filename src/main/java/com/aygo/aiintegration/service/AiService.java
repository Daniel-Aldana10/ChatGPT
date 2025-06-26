package com.aygo.aiintegration.service;

import com.aygo.aiintegration.ChatRequest;
import com.aygo.aiintegration.adapter.ChatGptAdapter;
import com.aygo.aiintegration.adapter.ChatGptAdapterProxy;
import com.aygo.aiintegration.analyzer.*;

import org.springframework.stereotype.Service;

/**
 * Service layer that orchestrates the ChatGPT flow.
 *
 * The input is processed as follows:
 *   1. ValidateInputProcessor: Validates, cleans, and improves the input.
 *   2. ChatGptFacade: Handles the AI call and error management.
 *   3. ChatGptAdapterProxy: Adds caching and fallback logic.
 *   4. ChatGptAdapter: Makes the actual call to OpenAI.
 */
@Service
public class AiService {

    private final ChatGptAdapterProxy proxy;

    public AiService(ChatGptAdapter chatGptAdapter) {
        this.proxy = new ChatGptAdapterProxy(chatGptAdapter);
    }

    /**
     * Processes the user input through validation, cleaning, and improvement,
     * then sends it to OpenAI and returns the response.
     *
     * @param input The user input wrapped in a ChatRequest
     * @return The AI's response as a String
     */
    public String generateResponse(ChatRequest input) {
        ValidateInputProcessor validate = new ValidateInputProcessor(5);
        ChatGptFacade facade = new ChatGptFacade(validate, proxy);
        return facade.getResponse(input);
    }
}
