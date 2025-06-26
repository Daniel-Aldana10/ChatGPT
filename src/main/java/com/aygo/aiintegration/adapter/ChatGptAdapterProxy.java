package com.aygo.aiintegration.adapter;

import com.aygo.aiintegration.ChatRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Proxy adapter that adds caching and fallback logic to the AI adapter.
 * Implements the IAiAdapter interface and wraps a real adapter instance.
 */
public class ChatGptAdapterProxy implements IAiAdapter {

    private final IAiAdapter realAdapter;
    private final Map<String, CachedResponse> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;
    private final String fallbackMessage;
    private final ScheduledExecutorService cacheCleaner;

    /**
     * Constructs a proxy with default TTL and fallback message.
     * @param realAdapter The real AI adapter to wrap
     */
    public ChatGptAdapterProxy(IAiAdapter realAdapter) {
        this(realAdapter, 5 * 60 * 1000, "[OpenAI no disponible, intente más tarde]");
    }

    /**
     * Constructs a proxy with custom TTL and fallback message.
     * @param realAdapter The real AI adapter to wrap
     * @param ttlMillis Time to live for cache entries (in milliseconds)
     * @param fallbackMessage Message to return if the real adapter fails
     */
    public ChatGptAdapterProxy(IAiAdapter realAdapter, long ttlMillis, String fallbackMessage) {
        this.realAdapter = realAdapter;
        this.ttlMillis = ttlMillis;
        this.fallbackMessage = fallbackMessage;
        // Iniciar limpieza automática cada 5 minutos
        this.cacheCleaner = Executors.newSingleThreadScheduledExecutor();
        this.cacheCleaner.scheduleAtFixedRate(this::clearExpiredCache, ttlMillis, ttlMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Generates a response using the real adapter, with caching and fallback logic.
     * @param input The user input wrapped in a ChatRequest
     * @return The AI's response as a String
     */
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

    /**
     * Returns the adapter status by delegating to the real adapter.
     * @return The status string
     */
    @Override
    public String getEstado() {
        return realAdapter.getEstado();
    }

    /**
     * Clear expired cache entries.
     */
    private void clearExpiredCache() {
        long now = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> (now - entry.getValue().timestamp) >= ttlMillis);
    }

    /**
     * Internal class for caching responses with timestamps.
     */
    private static class CachedResponse {
        String response;
        long timestamp;

        CachedResponse(String response, long timestamp) {
            this.response = response;
            this.timestamp = timestamp;
        }
    }
}