package com.aygo.aiintegration.analyzer;

public class ImproveInputProcessor implements PreProcessor {
    private PreProcessor next;
    private final boolean isCode;

    public ImproveInputProcessor(boolean isCode) {
        this.isCode = isCode;
    }

    @Override
    public String process(String input) {
        String improved = input;
        if (!isCode && input.length() < 100) {
            improved = "Por favor, responde de forma clara y concisa a la siguiente pregunta o petición: \""
                    + capitalizeAndPunctuate(input) + "\"";
        }
        if (next != null) {
            return next.process(improved);
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

    @Override
    public void setNext(PreProcessor next) {
        this.next = next;
    }
} 