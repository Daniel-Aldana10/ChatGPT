package com.aygo.aiintegration.analyzer;

import java.util.List;

public class ValidateInputProcessor{

    private final int minLength;

    public ValidateInputProcessor(int minLength) {
        this.minLength = minLength;
    }

    /**
     * Process, clean and improve entry before sending it to AI.
     * @param input user original entrance
     * @return validated, clean and improved entrance
     */
    public String process(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede estar vacía.");
        }

        // Limpieza básica
        String cleaned = input.replaceAll("[^a-zA-Z0-9áéíóúüñÁÉÍÓÚÜÑ\s]", "").trim();
        String normalized = cleaned.toLowerCase().strip();

        List<String> trivialInputs = List.of("hola", "adios", "como estas", "¿como estas");

        if (trivialInputs.contains(normalized)) {
            throw new IllegalArgumentException("La entrada es demasiado simple o trivial.");
        }

        if (cleaned.length() < minLength) {
            throw new IllegalArgumentException("La entrada debe tener al menos " + minLength + " caracteres.");
        }

        if (cleaned.matches("(?i).*(asdf|qwerty|123456|lorem ipsum).*")) {
            throw new IllegalArgumentException("La entrada parece ser basura o texto de prueba.");
        }

        String[] words = normalized.split("\\s+");
        if (words.length < 3) {
            throw new IllegalArgumentException("La entrada debe tener al menos 3 palabras.");
        }

        List<String> banned = List.of("idiota", "maldito", "estúpido");
        for (String word : banned) {
            if (normalized.contains(word)) {
                throw new IllegalArgumentException("La entrada contiene lenguaje inapropiado.");
            }
        }

        if (cleaned.matches("(?i).*\\b(\\w+)\\b(\\s+\\1\\b)+.*")) {
            throw new IllegalArgumentException("La entrada contiene repeticiones innecesarias.");
        }

        if (cleaned.length() > 1000) {
            throw new IllegalArgumentException("La entrada es demasiado larga.");
        }

        // Mejora básica: si es corto y no parece código, lo convierte en pregunta o petición clara
        String improved = cleaned;
        if (improved.length() < 100) {
            improved = "Por favor, responde de forma clara y concisa a la siguiente pregunta o petición: \""
                    + capitalizeAndPunctuate(improved) + "\"";
        }

        return improved;
    }

    private String capitalizeAndPunctuate(String text) {
        if (text == null || text.isEmpty()) return "";
        String trimmed = text.trim();
        String capitalized = trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1);
        if (!capitalized.endsWith("?") && !capitalized.endsWith(".")) {
            if (capitalized.startsWith("¿") || capitalized.toLowerCase().startsWith("que") || capitalized.toLowerCase().startsWith("cómo")) {
                capitalized += "?";
            } else {
                capitalized += ".";
            }
        }
        return capitalized;
    }
}
