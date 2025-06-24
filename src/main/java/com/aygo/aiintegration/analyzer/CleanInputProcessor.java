package com.aygo.aiintegration.analyzer;

public class CleanInputProcessor implements PreProcessor {
    private PreProcessor next;

    @Override
    public String process(String input) {
        String cleaned = input.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        if (next != null) {
            return next.process(cleaned);
        }
        return cleaned;
    }

    @Override
    public void setNext(PreProcessor next) {
        this.next = next;
    }
} 