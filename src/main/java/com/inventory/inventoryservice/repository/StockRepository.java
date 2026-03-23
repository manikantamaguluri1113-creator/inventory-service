package com.inventory.inventoryservice.repository;

import com.inventory.inventoryservice.entity.StockItem;
import com.inventory.inventoryservice.entity.StockItem.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockItem, String> {

    List<StockItem> findByWarehouseId(String warehouseId);

    Optional<StockItem> findByProductIdAndWarehouseId(String productId, String warehouseId);

    List<StockItem> findByStatus(StockStatus status);

    List<StockItem> findByQuantityLessThanEqual(Integer threshold);
}