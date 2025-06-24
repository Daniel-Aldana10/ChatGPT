package com.aygo.aiintegration.service;

import com.aygo.aiintegration.adapter.IAiAdapter;
import com.aygo.aiintegration.analyzer.InputAnalyzer;
import com.aygo.aiintegration.adapter.ChatGptAdapterProxy;
import com.aygo.aiintegration.analyzer.CleanInputProcessor;
import com.aygo.aiintegration.analyzer.ImproveInputProcessor;
import com.aygo.aiintegration.analyzer.PreProcessor;
import com.aygo.aiintegration.analyzer.ValidateInputProcessor;
import com.aygo.aiintegration.analyzer.ChatGptFacade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {

    private final List<IAiAdapter> adapters;

    public AiService(List<IAiAdapter> adapters) {
        this.adapters = adapters;

        adapters.stream()
                .filter(adapter -> adapter.getEstado().equals("general"))
                .findFirst()
                .ifPresent(InputAnalyzer::setChatGptAdapter);
    }

    public String generateResponse(String input) {
        boolean isCode = InputAnalyzer.isCode(input);
        if (!isCode) {
            // Cadena de responsabilidad mejorada
            PreProcessor validate = new ValidateInputProcessor(5); // mínimo 5 caracteres
            PreProcessor clean = new CleanInputProcessor();
            PreProcessor improve = new ImproveInputProcessor(false);
            validate.setNext(clean);
            clean.setNext(improve);

            // Proxy mejorado
            IAiAdapter realAdapter = adapters.stream()
                    .filter(a -> a.getEstado().equals("general"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No suitable adapter found."));
            ChatGptAdapterProxy proxy = new ChatGptAdapterProxy(realAdapter);

            // Fachada
            ChatGptFacade facade = new ChatGptFacade(validate, proxy);
            return facade.getResponse(input);
        } else {
            // Flujo para código (Copilot u otro)
            IAiAdapter adapter = adapters.stream()
                    .filter(a -> a.getEstado().equals("código"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No suitable adapter found."));
            return adapter.generateResponse(input);
        }
    }
}
