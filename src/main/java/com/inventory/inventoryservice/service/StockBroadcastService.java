package com.inventory.inventoryservice.service;

import com.inventory.inventoryservice.entity.StockItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockBroadcastService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastStockUpdate(StockItem item) {
    	System.out.println("Broadcasting stock update for: " + item.getProductName());
        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", item.getProductId());
        payload.put("productName", item.getProductName());
        payload.put("warehouseId", item.getWarehouseId());
        payload.put("warehouseName", item.getWarehouseName());
        payload.put("quantity", item.getQuantity());
        payload.put("status", item.getStatus().name());
        payload.put("lastUpdated", item.getLastUpdated().toString());

        messagingTemplate.convertAndSend("/topic/stock", payload);
        messagingTemplate.convertAndSend(
            "/topic/stock/" + item.getWarehouseId(), payload);
    }
}