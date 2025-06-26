package com.aygo.aiintegration.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InputAnalyzer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Extract the relevant content of the JSON response from OpenAI.
     * @param response Respuesta en formato JSON de Open
     * @return extracted textual content or error message
     */
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
