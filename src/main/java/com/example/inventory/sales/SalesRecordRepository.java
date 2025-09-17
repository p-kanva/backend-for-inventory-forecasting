package com.example.inventory.sales;

import com.example.inventory.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
    List<SalesRecord> findByProductAndSaleDateBetween(Product product, LocalDate start, LocalDate end);

    List<SalesRecord> findByProduct(Product product);
}
