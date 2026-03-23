package com.inventory.inventoryservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_items")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String warehouseId;

    @Column(nullable = false)
    private String warehouseName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reorderThreshold;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private StockStatus status;

    private LocalDateTime lastUpdated;

    public enum StockStatus {
        NORMAL, LOW, OUT_OF_STOCK
    }

    public StockItem() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getReorderThreshold() { return reorderThreshold; }
    public void setReorderThreshold(Integer reorderThreshold) { this.reorderThreshold = reorderThreshold; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public StockStatus getStatus() { return status; }
    public void setStatus(StockStatus status) { this.status = status; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}