package com.example.inventory.order;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/auto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseOrder>> auto() {
        return ResponseEntity.ok(service.autoGenerate());
    }

    @PutMapping("/{id}/receive")
    public ResponseEntity<PurchaseOrder> receive(@PathVariable Long id) {
        return ResponseEntity.ok(service.receive(id));
    }
}
