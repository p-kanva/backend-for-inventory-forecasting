package com.example.inventory.order;

import com.example.inventory.forecast.ForecastRepository;
import com.example.inventory.product.Product;
import com.example.inventory.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final PurchaseOrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final ForecastRepository forecastRepo;

    public OrderService(PurchaseOrderRepository orderRepo, ProductRepository productRepo, ForecastRepository forecastRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.forecastRepo = forecastRepo;
    }

    @Transactional
    public List<PurchaseOrder> autoGenerate() {
        List<Product> products = productRepo.findAll();
        List<PurchaseOrder> created = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Product p : products) {
            if (p.getCurrentStock() < p.getReorderThreshold()) {
                int latestForecast = forecastRepo.findTop1ByProductOrderByForecastDateDesc(p)
                        .map(f -> f.getPredictedDemand()).orElse(p.getReorderThreshold());
                int need = Math.max(p.getReorderThreshold() - p.getCurrentStock(), latestForecast * Math.max(1, p.getLeadTimeDays()));
                PurchaseOrder po = new PurchaseOrder();
                po.setProduct(p);
                po.setOrderDate(today);
                po.setQuantityOrdered(Math.max(1, need));
                po.setExpectedArrivalDate(today.plusDays(Math.max(1, p.getLeadTimeDays())));
                orderRepo.save(po);
                created.add(po);
            }
        }
        return created;
    }

    @Transactional
    public PurchaseOrder receive(Long orderId) {
        PurchaseOrder po = orderRepo.findById(orderId).orElseThrow();
        if (po.getStatus() == PurchaseOrder.Status.RECEIVED) return po;
        po.setStatus(PurchaseOrder.Status.RECEIVED);
        Product p = po.getProduct();
        p.setCurrentStock(p.getCurrentStock() + po.getQuantityOrdered());
        productRepo.save(p);
        return orderRepo.save(po);
    }
}
