package com.inventory.inventoryservice.service;

import com.inventory.inventoryservice.entity.AuditLog;
import com.inventory.inventoryservice.entity.StockItem;
import com.inventory.inventoryservice.entity.StockItem.StockStatus;
import com.inventory.inventoryservice.repository.AuditLogRepository;
import com.inventory.inventoryservice.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @Autowired
    private StockBroadcastService broadcastService;

    @Override
    public List<StockItem> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public List<StockItem> getStockByWarehouse(String warehouseId) {
        return stockRepository.findByWarehouseId(warehouseId);
    }

    @Override
    public Optional<StockItem> getStockById(String id) {
        return stockRepository.findById(id);
    }
    
    @Scheduled(fixedRate = 300000) // every 5 minutes
    @Transactional
    public void processReorderScheduler() {

        List<StockItem> items = stockRepository.findAll();

        for (StockItem item : items) {

            int qty = item.getQuantity();
            int threshold = item.getReorderThreshold();

            LocalDateTime lastUpdated = item.getLastUpdated();
            LocalDateTime now = LocalDateTime.now();

            //  Time difference in hours
            long hours = java.time.Duration.between(lastUpdated, now).toHours();

            //  Case 1: OUT OF STOCK → 12 hrs
            if (qty == 0 && hours >= 12) {

                triggerReorder(item, "OUT_OF_STOCK");

            }

            //  Case 2: LOW STOCK → 24 hrs
            else if (qty <= threshold && hours >= 24) {

                triggerReorder(item, "LOW_STOCK");
            }
        }
    }
    
    private void triggerReorder(StockItem item, String reason) {

        int threshold = item.getReorderThreshold();
        int currentQty = item.getQuantity();

        int reorderQty = (threshold * 2)- currentQty;

        item.setQuantity(currentQty + reorderQty);
        item.setLastUpdated(LocalDateTime.now());
        item.setStatus(resolveStatus(item.getQuantity(), threshold));

        StockItem saved = stockRepository.save(item);

        broadcastService.broadcastStockUpdate(saved);

        System.out.println("REORDER (" + reason + ") for " + item.getProductName());

        auditLogRepository.save(
                AuditLog.of(saved, currentQty, reorderQty)
        );
    }

    @Override
    @Transactional
    public StockItem adjustStock(String productId, String warehouseId, int delta) {
        StockItem item = stockRepository
                .findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        int previousQty = item.getQuantity();
        item.setQuantity(previousQty + delta);
        item.setLastUpdated(LocalDateTime.now());
        item.setStatus(resolveStatus(item.getQuantity(), item.getReorderThreshold()));

        StockItem saved = stockRepository.save(item);
        broadcastService.broadcastStockUpdate(saved);
        auditLogRepository.save(AuditLog.of(saved, previousQty, delta));

        return saved;
    }
    
//    @Override
//    @Transactional
//    public StockItem adjustStock(String productId, String warehouseId, int delta) {
//
//        StockItem item = stockRepository
//                .findByProductIdAndWarehouseId(productId, warehouseId)
//                .orElseThrow(() -> new RuntimeException("Stock not found"));
//
//        int previousQty = item.getQuantity();
//        int threshold = item.getReorderThreshold();
//
//        // Step 1: Apply change
//        int newQty = previousQty + delta;
//
//        // Step 2: Check if threshold is crossed (IMPORTANT FIX)
//        if (previousQty > threshold && newQty <= threshold) {
//
//            int reorderQty = threshold - newQty; // bring back to threshold
//
//            newQty = newQty + reorderQty;
//
//            System.out.println("AUTO REORDER TRIGGERED for " + item.getProductName());
//
//            // Optional audit for reorder
//            auditLogRepository.save(
//                    AuditLog.of(item, previousQty, reorderQty)
//            );
//        }
//
//        // Step 3: Update entity
//        item.setQuantity(newQty);
//        item.setLastUpdated(LocalDateTime.now());
//        item.setStatus(resolveStatus(newQty, threshold));
//
//        StockItem saved = stockRepository.save(item);
//
//        // Step 4: Broadcast
//        broadcastService.broadcastStockUpdate(saved);
//
//        // Step 5: Original audit log
//        auditLogRepository.save(AuditLog.of(saved, previousQty, delta));
//
//        return saved;
//    }
    @Override
    @Transactional
    public StockItem createStock(StockItem item) {
        item.setLastUpdated(LocalDateTime.now());
        item.setStatus(resolveStatus(item.getQuantity(), item.getReorderThreshold()));
        return stockRepository.save(item);
    }

    @Override
    @Transactional
    public StockItem updateStock(String id, StockItem updatedItem) {
        StockItem existing = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        existing.setProductName(updatedItem.getProductName());
        existing.setQuantity(updatedItem.getQuantity());
        existing.setReorderThreshold(updatedItem.getReorderThreshold());
        existing.setLastUpdated(LocalDateTime.now());
        existing.setStatus(resolveStatus(
                existing.getQuantity(),
                existing.getReorderThreshold()));
        
        StockItem saved = stockRepository.save(existing);
        broadcastService.broadcastStockUpdate(saved);
        return saved;

        //return stockRepository.save(existing);
    }

    @Override
    public void deleteStock(String id) {
        stockRepository.deleteById(id);
    }

    @Override
    public List<StockItem> getLowStockItems() {
        return stockRepository.findByStatus(StockStatus.LOW);
    }

    @Override
    public List<StockItem> getOutOfStockItems() {
        return stockRepository.findByStatus(StockStatus.OUT_OF_STOCK);
    }

    private StockStatus resolveStatus(int qty, int threshold) {
        if (qty <= 0) return StockStatus.OUT_OF_STOCK;
        if (qty <= threshold) return StockStatus.LOW;
        return StockStatus.NORMAL;
    }
}