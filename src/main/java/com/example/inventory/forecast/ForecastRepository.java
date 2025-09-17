package com.example.inventory.forecast;

import com.example.inventory.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {
    List<Forecast> findByForecastDateBetween(LocalDate start, LocalDate end);

    Optional<Forecast> findTop1ByProductOrderByForecastDateDesc(Product product);
}
