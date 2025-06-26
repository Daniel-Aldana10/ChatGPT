package com.aygo.aiintegration.adapter;

import com.aygo.aiintegration.ChatRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Adapter for communicating with the OpenAI ChatGPT API.
 * Implements the IAiAdapter interface to provide a unified way to generate responses.
 */
public class ChatGptAdapter implements IAiAdapter{

    private final WebClient webClient;
    private final String apiKey;
    private final String apiUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a new ChatGptAdapter.
     * @param webClient The WebClient instance for HTTP requests
     * @param apiKey The OpenAI API key
     * @param apiUrl The OpenAI API endpoint URL
     */
    public ChatGptAdapter(WebClient webClient, String apiKey, String apiUrl) {
        this.webClient = webClient;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    /**
     * Sends a prompt to the OpenAI API and returns the raw response as a String.
     * @param input The user input wrapped in a ChatRequest
     * @return The raw response from OpenAI as a String
     */
    @Override
    public String generateResponse(ChatRequest input) {
        try {
            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(
                            Map.of("role", "system", "content", "Eres un asistente útil."),
                            Map.of("role", "user", "content", input.getInput())
                    ),
                    "max_tokens", 1000,
                    "temperature", 0.7
            ));

            return webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Error llamando a OpenAI: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the adapter status. (Currently always returns "general")
     * @return The status string
     */
    @Override
    public String getEstado() {
        return "general";
    }
}