package com.example.inventory.forecast;

import com.example.inventory.product.Product;
import com.example.inventory.product.ProductRepository;
import com.example.inventory.sales.SalesRecord;
import com.example.inventory.sales.SalesRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ForecastServiceTest {
    private ForecastRepository forecastRepository;
    private SalesRecordRepository salesRepository;
    private ProductRepository productRepository;
    private ForecastService service;

    @BeforeEach
    void setup() {
        forecastRepository = Mockito.mock(ForecastRepository.class);
        salesRepository = Mockito.mock(SalesRecordRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        service = new ForecastService(forecastRepository, salesRepository, productRepository);
    }

    @Test
    void testRecalculateMovingAverage() {
        Product p = new Product();
        p.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(salesRepository.findByProductAndSaleDateBetween(eq(p), any(), any()))
                .thenReturn(List.of(
                        mkSale(p, 5), mkSale(p, 7), mkSale(p, 6), mkSale(p, 0), mkSale(p, 4), mkSale(p, 3), mkSale(p, 5)
                ));
        service.recalculateForProduct(1L);
        verify(forecastRepository, times(1)).save(any());
    }

    private SalesRecord mkSale(Product p, int qty) {
        SalesRecord sr = new SalesRecord();
        sr.setProduct(p);
        sr.setQuantity(qty);
        sr.setSaleDate(LocalDate.now());
        return sr;
    }
}
