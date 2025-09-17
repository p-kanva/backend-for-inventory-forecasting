package com.example.inventory.order;

import com.example.inventory.order.PurchaseOrder.Status;
import com.example.inventory.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByProductAndStatus(Product product, Status status);
}
