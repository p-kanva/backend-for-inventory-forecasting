package com.example.inventory.order;

import com.example.inventory.forecast.Forecast;
import com.example.inventory.forecast.ForecastRepository;
import com.example.inventory.product.Product;
import com.example.inventory.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private PurchaseOrderRepository orderRepo;
    private ProductRepository productRepo;
    private ForecastRepository forecastRepo;
    private OrderService service;

    @BeforeEach
    void setup() {
        orderRepo = Mockito.mock(PurchaseOrderRepository.class);
        productRepo = Mockito.mock(ProductRepository.class);
        forecastRepo = Mockito.mock(ForecastRepository.class);
        service = new OrderService(orderRepo, productRepo, forecastRepo);
    }

    @Test
    void testAutoGeneratesWhenBelowThreshold() {
        var p = new Product();
        p.setId(1L); p.setCurrentStock(5); p.setReorderThreshold(10); p.setLeadTimeDays(2);
        when(productRepo.findAll()).thenReturn(List.of(p));
        Forecast f = new Forecast();
        f.setPredictedDemand(3);
        when(forecastRepo.findTop1ByProductOrderByForecastDateDesc(p)).thenReturn(Optional.of(f));
        when(orderRepo.save(any())).thenAnswer(a -> a.getArguments()[0]);

        var created = service.autoGenerate();
        assertEquals(1, created.size());
        assertEquals(PurchaseOrder.Status.PENDING, created.get(0).getStatus());
    }
}
