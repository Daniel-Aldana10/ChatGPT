package com.aygo.aiintegration.analyzer;

import com.aygo.aiintegration.ChatRequest;
import com.aygo.aiintegration.adapter.IAiAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InputAnalyzer {

    private static IAiAdapter chatGptAdapter;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setChatGptAdapter(IAiAdapter adapter) {
        chatGptAdapter = adapter;
    }
    
    

    public static boolean isCode(ChatRequest input) {
        if (chatGptAdapter != null) {
            String query = "Esto es código o tiene que ver con código? Responde si o no sin mas texto" + input.getInput();
            ChatRequest chatRequest = new ChatRequest();
            chatRequest.setInput(query);
            String response = chatGptAdapter.generateResponse(chatRequest);
            return (extractContentFromResponse(response).toLowerCase().contains("sí")) ;
        }
        return false;
    }

    public static String cleanInput(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
    }
    
    public static String improveInput(String input, Boolean isCode) {
        if (chatGptAdapter != null && input.length() < 100 && !isCode) {
            String query = "Mejora esta entrada como un prompt para una IA: " + input;
            ChatRequest chatRequest = new ChatRequest();
            chatRequest.setInput(query);
            String response = chatGptAdapter.generateResponse(chatRequest);
            String content = extractContentFromResponse(response);
            return content != null ? content : input;
        }
        return input;
    
    }
    public static String extractContentFromResponse(String response) {
        try {
            // Verifica si la respuesta es JSON
            if (!response.trim().contains("{")) {
                System.err.println("Respuesta no válida para JSON: " + response);
                return "Error: Respuesta no válida de OpenAI debido a que " + response ;
            }

            JsonNode rootNode = objectMapper.readTree(response);

            // Manejo de error de OpenAI
            if (rootNode.has("error")) {
                String errorMsg = rootNode.path("error").path("message").asText();
                System.err.println("Error de OpenAI: " + errorMsg);
                return "Error de OpenAI: " + errorMsg;
            }

            JsonNode messageNode = rootNode.path("choices").get(0).path("message").path("content");
            return messageNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: No se pudo procesar la respuesta de OpenAI";
        }
    }

}
