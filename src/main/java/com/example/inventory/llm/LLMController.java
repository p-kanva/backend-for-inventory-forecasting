package com.example.inventory.llm;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/llm")
public class LLMController {
    private final LLMService llmService;
    public LLMController(LLMService llmService) { this.llmService = llmService; }

    @PostMapping("/parse")
    public ResponseEntity<ParsedData> parse(@Valid @RequestBody ParseRequest req) {
        return ResponseEntity.ok(llmService.parse(req.data));
    }
}
