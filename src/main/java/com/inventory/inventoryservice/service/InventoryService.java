package com.inventory.inventoryservice.service;

import com.inventory.inventoryservice.entity.StockItem;
import java.util.List;
import java.util.Optional;

public interface InventoryService {

    List<StockItem> getAllStock();

    List<StockItem> getStockByWarehouse(String warehouseId);

    Optional<StockItem> getStockById(String id);

    StockItem adjustStock(String productId, String warehouseId, int delta);

    StockItem createStock(StockItem item);

    StockItem updateStock(String id, StockItem updatedItem);

    void deleteStock(String id);

    List<StockItem> getLowStockItems();

    List<StockItem> getOutOfStockItems();
}