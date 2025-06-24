package com.aygo.aiintegration.adapter;

import com.aygo.aiintegration.ChatRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatGptAdapterProxy implements IAiAdapter {

    private final IAiAdapter realAdapter;
    private final Map<String, CachedResponse> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;
    private final String fallbackMessage;

    public ChatGptAdapterProxy(IAiAdapter realAdapter) {
        this(realAdapter, 5 * 60 * 1000, "[OpenAI no disponible, intente más tarde]");
    }

    public ChatGptAdapterProxy(IAiAdapter realAdapter, long ttlMillis, String fallbackMessage) {
        this.realAdapter = realAdapter;
        this.ttlMillis = ttlMillis;
        this.fallbackMessage = fallbackMessage;
    }

    @Override
    public String generateResponse(ChatRequest input) {
        long now = System.currentTimeMillis();
        CachedResponse cached = cache.get(input.getInput());

        if (cached != null && (now - cached.timestamp) < ttlMillis) {
            System.out.println("[Proxy] Respuesta cacheada para: " + input.getInput());
            return cached.response;
        }

        try {
            long start = System.currentTimeMillis();
            String response = realAdapter.generateResponse(input);
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("[Proxy] Llamada a OpenAI para: '" + input.getInput() + "' en " + elapsed + "ms");

            cache.put(input.getInput(), new CachedResponse(response, now));
            return response;
        } catch (Exception e) {
            System.err.println("[Proxy] Error llamando a OpenAI: " + e.getMessage());
            return fallbackMessage;
        }
    }

    @Override
    public String getEstado() {
        return realAdapter.getEstado();
    }

    private static class CachedResponse {
        String response;
        long timestamp;

        CachedResponse(String response, long timestamp) {
            this.response = response;
            this.timestamp = timestamp;
        }
    }
}