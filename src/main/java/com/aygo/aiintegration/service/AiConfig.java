
package com.aygo.aiintegration.service;

import com.aygo.aiintegration.adapter.ChatGptAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiConfig {

    @Value("${api.chatgpt.key}")
    private String apiKey;

    @Value("${api.chatgpt.url}")
    private String apiUrl;

    @Bean
    public ChatGptAdapter chatGptAdapter(WebClient.Builder builder) {
        return new ChatGptAdapter(builder.build(), apiKey, apiUrl);
    }
}
