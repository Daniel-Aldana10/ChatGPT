package com.aygo.aiintegration.controller;

import com.aygo.aiintegration.ChatRequest;
import com.aygo.aiintegration.service.AiService;
import com.aygo.aiintegration.analyzer.InputAnalyzer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody ChatRequest input) {
        if (input == null || input.getInput() == null || input.getInput().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El campo 'input' no puede estar vacío.");
        }

        try {
            String rawResponse = aiService.generateResponse(input);
            String cleanResponse = InputAnalyzer.extractContentFromResponse(rawResponse);
            return ResponseEntity.ok(cleanResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Entrada inválida: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }
}