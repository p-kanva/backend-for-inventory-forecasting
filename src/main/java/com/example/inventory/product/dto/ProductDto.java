package com.example.inventory.product.dto;

public class ProductDto {
    public Long id;
    public String name;
    public String category;
    public int currentStock;
    public int reorderThreshold;
    public int leadTimeDays;
    public int latestForecast;
}
