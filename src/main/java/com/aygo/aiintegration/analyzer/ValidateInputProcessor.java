package com.aygo.aiintegration.analyzer;

public class ValidateInputProcessor implements PreProcessor {
    private PreProcessor next;
    private final int minLength;

    public ValidateInputProcessor(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public String process(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede estar vacía.");
        }
        if (input.length() < minLength) {
            throw new IllegalArgumentException("La entrada debe tener al menos " + minLength + " caracteres.");
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