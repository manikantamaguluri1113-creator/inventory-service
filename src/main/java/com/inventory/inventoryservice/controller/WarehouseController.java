package com.inventory.inventoryservice.controller;

import com.inventory.inventoryservice.entity.StockItem;
import com.inventory.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventory/warehouses")
@CrossOrigin(origins = "http://localhost:4200")
public class WarehouseController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<String>> getAllWarehouses() {
        List<StockItem> allStock = inventoryService.getAllStock();
        List<String> warehouses = allStock.stream()
                .map(StockItem::getWarehouseId)
                .distinct()
                .toList();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{warehouseId}/stock")
    public ResponseEntity<List<StockItem>> getStockByWarehouse(
            @PathVariable String warehouseId) {
        return ResponseEntity.ok(
                inventoryService.getStockByWarehouse(warehouseId));
    }
}