package com.inventory.inventoryservice.controller;

import com.inventory.inventoryservice.entity.StockItem;
import com.inventory.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory/stock")
@CrossOrigin(origins = "http://localhost:4200")
public class StockController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<StockItem>> getAllStock() {
        return ResponseEntity.ok(inventoryService.getAllStock());
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<StockItem>> getStockByWarehouse(
            @PathVariable String warehouseId) {
        return ResponseEntity.ok(inventoryService.getStockByWarehouse(warehouseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockItem> getStockById(@PathVariable String id) {
        return inventoryService.getStockById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockItem> createStock(@RequestBody StockItem item) {
        StockItem created = inventoryService.createStock(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockItem> updateStock(
            @PathVariable String id,
            @RequestBody StockItem item) {
        return ResponseEntity.ok(inventoryService.updateStock(id, item));
    }

    @PatchMapping("/{productId}/adjust")
    public ResponseEntity<StockItem> adjustStock(
            @PathVariable String productId,
            @RequestBody Map<String, Object> body) {
        String warehouseId = (String) body.get("warehouseId");
        int delta = (Integer) body.get("delta");
        return ResponseEntity.ok(
                inventoryService.adjustStock(productId, warehouseId, delta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable String id) {
        inventoryService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<StockItem>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<StockItem>> getOutOfStockItems() {
        return ResponseEntity.ok(inventoryService.getOutOfStockItems());
    }
}