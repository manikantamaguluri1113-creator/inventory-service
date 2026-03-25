package com.inventory.inventoryservice.service;

import com.inventory.inventoryservice.entity.AuditLog;
import com.inventory.inventoryservice.entity.StockItem;
import com.inventory.inventoryservice.entity.StockItem.StockStatus;
import com.inventory.inventoryservice.repository.AuditLogRepository;
import com.inventory.inventoryservice.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
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