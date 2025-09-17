package com.example.inventory.sales;

import com.example.inventory.sales.dto.SalesDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    private final SalesService service;

    public SalesController(SalesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SalesRecord> record(@Valid @RequestBody SalesDto dto) {
        return ResponseEntity.ok(service.recordSale(dto));
    }
}
