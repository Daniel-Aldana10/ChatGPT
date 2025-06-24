package com.aygo.aiintegration.adapter;

import com.aygo.aiintegration.ChatRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

public class ChatGptAdapter implements IAiAdapter{

    private final WebClient webClient;
    private final String apiKey;
    private final String apiUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatGptAdapter(WebClient webClient, String apiKey, String apiUrl) {
        this.webClient = webClient;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }



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


    @Override
    public String getEstado() {
        return "general";
    }
}