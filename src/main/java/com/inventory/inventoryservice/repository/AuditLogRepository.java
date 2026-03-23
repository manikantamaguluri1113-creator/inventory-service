package com.inventory.inventoryservice.repository;

import com.inventory.inventoryservice.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    List<AuditLog> findByProductId(String productId);

    List<AuditLog> findByWarehouseId(String warehouseId);
}