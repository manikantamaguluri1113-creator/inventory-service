package com.inventory.inventoryservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;
    private String productId;
    private String warehouseId;
    private int previousQuantity;
    private int delta;
    private int newQuantity;
    private String status;
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(String id, String productId, String warehouseId,
                    int previousQuantity, int delta, int newQuantity,
                    String status, LocalDateTime timestamp) {
        this.id = id;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.previousQuantity = previousQuantity;
        this.delta = delta;
        this.newQuantity = newQuantity;
        this.status = status;
        this.timestamp = timestamp;
    }

    public static AuditLog of(StockItem item, int previousQty, int delta) {
        AuditLog log = new AuditLog();
        log.productId = item.getProductId();
        log.warehouseId = item.getWarehouseId();
        log.previousQuantity = previousQty;
        log.delta = delta;
        log.newQuantity = item.getQuantity();
        log.status = item.getStatus().name();
        log.timestamp = LocalDateTime.now();
        return log;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public int getPreviousQuantity() { return previousQuantity; }
    public void setPreviousQuantity(int previousQuantity) { this.previousQuantity = previousQuantity; }

    public int getDelta() { return delta; }
    public void setDelta(int delta) { this.delta = delta; }

    public int getNewQuantity() { return newQuantity; }
    public void setNewQuantity(int newQuantity) { this.newQuantity = newQuantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}