package com.programming.techie.inventoryservice.service;

import com.programming.techie.inventoryservice.dto.InventoryResponse;
import com.programming.techie.inventoryservice.model.Inventory;
import com.programming.techie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes){
        List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCodes);

         return inventoryList.stream()
                .map(this::mapToInventoryResponse)
                 .toList();
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory){
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
