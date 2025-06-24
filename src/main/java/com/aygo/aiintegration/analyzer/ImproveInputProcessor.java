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
            // Aquí podrías llamar a un método de mejora real, por ahora solo simula
            improved = "[Mejorado] " + input;
        }
        if (next != null) {
            return next.process(improved);
        }
        return improved;
    }

    @Override
    public void setNext(PreProcessor next) {
        this.next = next;
    }
} 