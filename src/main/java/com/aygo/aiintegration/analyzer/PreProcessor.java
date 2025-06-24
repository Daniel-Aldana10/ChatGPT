package com.aygo.aiintegration.analyzer;

public interface PreProcessor {
    String process(String input);
    void setNext(PreProcessor next);
} 