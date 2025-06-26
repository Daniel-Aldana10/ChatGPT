package com.aygo.aiintegration.adapter;

import com.aygo.aiintegration.ChatRequest;

/**
 * Interface for AI adapters that can generate responses and report status.
 */
public interface IAiAdapter {
    /**
     * Generates a response for the given input.
     * @param input The user input wrapped in a ChatRequest
     * @return The AI's response as a String
     */
    String generateResponse(ChatRequest input);
    /**
     * Returns the adapter status.
     * @return The status string
     */
    String getEstado();
}
