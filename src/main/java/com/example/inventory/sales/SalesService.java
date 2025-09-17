package com.example.inventory.sales;

import com.example.inventory.product.Product;
import com.example.inventory.product.ProductRepository;
import com.example.inventory.sales.dto.SalesDto;
import com.example.inventory.forecast.ForecastService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalesService {
    private final SalesRecordRepository salesRepo;
    private final ProductRepository productRepo;
    private final ForecastService forecastService;

    public SalesService(SalesRecordRepository salesRepo, ProductRepository productRepo, ForecastService forecastService) {
        this.salesRepo = salesRepo;
        this.productRepo = productRepo;
        this.forecastService = forecastService;
    }

    @Transactional
    public SalesRecord recordSale(SalesDto dto) {
        Product p = productRepo.findById(dto.productId).orElseThrow();
        SalesRecord sr = new SalesRecord();
        sr.setProduct(p);
        sr.setQuantity(dto.quantity);
        sr.setLocation(dto.location);
        sr.setSaleDate(dto.saleDate);
        salesRepo.save(sr);
        forecastService.recalculateForProduct(p.getId());
        p.setCurrentStock(Math.max(0, p.getCurrentStock() - dto.quantity));
        productRepo.save(p);
        return sr;
    }
}
