package com.inventory.inventoryservice.controller;

import com.inventory.inventoryservice.entity.StockItem;
import com.inventory.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/inventory/alerts")
@CrossOrigin(origins = "http://localhost:4200")
public class AlertsController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();

        List<StockItem> lowStock = inventoryService.getLowStockItems();
        List<StockItem> outOfStock = inventoryService.getOutOfStockItems();

        for (StockItem item : outOfStock) {
            Map<String, Object> alert = new HashMap<>();
            alert.put("id", item.getId());
            alert.put("product", item.getProductName());
            alert.put("warehouse", item.getWarehouseName());
            alert.put("quantity", item.getQuantity());
            alert.put("type", "OUT_OF_STOCK");
            alert.put("icon", "error");
            alert.put("time", item.getLastUpdated().toString());
            alerts.add(alert);
        }

        for (StockItem item : lowStock) {
            Map<String, Object> alert = new HashMap<>();
            alert.put("id", item.getId());
            alert.put("product", item.getProductName());
            alert.put("warehouse", item.getWarehouseName());
            alert.put("quantity", item.getQuantity());
            alert.put("type", "LOW");
            alert.put("icon", "warning");
            alert.put("time", item.getLastUpdated().toString());
            alerts.add(alert);
        }

        return ResponseEntity.ok(alerts);
    }
}