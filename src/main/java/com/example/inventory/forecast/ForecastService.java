package com.example.inventory.forecast;

import com.example.inventory.product.Product;
import com.example.inventory.product.ProductRepository;
import com.example.inventory.sales.SalesRecord;
import com.example.inventory.sales.SalesRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class ForecastService {
    private final ForecastRepository forecastRepository;
    private final SalesRecordRepository salesRepository;
    private final ProductRepository productRepository;

    @Value("${app.forecast.windowDays:7}")
    private int windowDays;

    public ForecastService(ForecastRepository forecastRepository, SalesRecordRepository salesRepository, ProductRepository productRepository) {
        this.forecastRepository = forecastRepository;
        this.salesRepository = salesRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void recalculateForProduct(Long productId) {
        Product p = productRepository.findById(productId).orElseThrow();
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(windowDays - 1L);
        List<SalesRecord> sales = salesRepository.findByProductAndSaleDateBetween(p, start, end);
        int total = sales.stream().mapToInt(SalesRecord::getQuantity).sum();
        int avgPerDay = Math.round(total / (float) windowDays);
        Forecast f = new Forecast();
        f.setProduct(p);
        f.setForecastDate(end.plusDays(1));
        f.setPredictedDemand(Math.max(0, avgPerDay));
        forecastRepository.save(f);
    }

    public List<Forecast> listInRange(LocalDate start, LocalDate end) {
        return forecastRepository.findByForecastDateBetween(start, end).stream()
                .sorted(Comparator.comparing(Forecast::getForecastDate)).toList();
    }
}
