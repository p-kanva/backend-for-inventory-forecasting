package com.example.inventory.product;

import com.example.inventory.forecast.Forecast;
import com.example.inventory.forecast.ForecastRepository;
import com.example.inventory.product.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repo;
    private final ForecastRepository forecastRepository;

    public ProductService(ProductRepository repo, ForecastRepository forecastRepository) {
        this.repo = repo;
        this.forecastRepository = forecastRepository;
    }

    public Product create(Product p) {
        return repo.save(p);
    }

    public ProductDto getWithLatestForecast(Long id) {
        Product p = repo.findById(id).orElseThrow();
        ProductDto dto = new ProductDto();
        dto.id = p.getId();
        dto.name = p.getName();
        dto.category = p.getCategory();
        dto.currentStock = p.getCurrentStock();
        dto.reorderThreshold = p.getReorderThreshold();
        dto.leadTimeDays = p.getLeadTimeDays();
        Optional<Forecast> f = forecastRepository.findTop1ByProductOrderByForecastDateDesc(p);
        dto.latestForecast = f.map(Forecast::getPredictedDemand).orElse(0);
        return dto;
    }
}
