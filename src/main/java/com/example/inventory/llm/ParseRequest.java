package com.example.inventory.llm;

import jakarta.validation.constraints.NotBlank;

public class ParseRequest {
    @NotBlank
    public String data;
}
