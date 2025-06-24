package com.aygo.aiintegration.adapter;


import com.aygo.aiintegration.ChatRequest;

public interface IAiAdapter {
    String generateResponse(ChatRequest input);
    String getEstado();
}
