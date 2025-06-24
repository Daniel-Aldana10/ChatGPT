package com.aygo.aiintegration.analyzer;

import java.util.List;

public class ValidateInputProcessor implements PreProcessor {

    private final int minLength;
    private PreProcessor next;

    public ValidateInputProcessor(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public String process(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede estar vacía.");
        }

        String trimmed = input.trim();
        String normalized = trimmed.toLowerCase().replaceAll("[^a-záéíóúüñ\\s]", "").strip();

        List<String> trivialInputs = List.of("hola", "adios", "como estas", "¿como estas");

        if (trivialInputs.contains(normalized)) {
            throw new IllegalArgumentException("La entrada es demasiado simple o trivial.");
        }

        if (input.length() < minLength) {
            throw new IllegalArgumentException("La entrada debe tener al menos " + minLength + " caracteres.");
        }

        if (input.matches("(?i).*(asdf|qwerty|123456|lorem ipsum).*")) {
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


        if (input.matches("(?i).*\\b(\\w+)\\b(\\s+\\1\\b)+.*")) {
            throw new IllegalArgumentException("La entrada contiene repeticiones innecesarias.");
        }

        if (input.length() > 1000) {
            throw new IllegalArgumentException("La entrada es demasiado larga.");
        }

        if (next != null) {
            return next.process(input);
        }

        return input;
    }

    @Override
    public void setNext(PreProcessor next) {
        this.next = next;
    }
}
