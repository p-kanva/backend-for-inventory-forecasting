package com.example.inventory.sales.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SalesDto {
    @NotNull
    public Long productId;
    @NotNull
    public Integer quantity;
    public String location;
    @NotNull
    public LocalDate saleDate;
}
