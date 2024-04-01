package com.programming.techie.orderservice.service;

import com.programming.techie.orderservice.dto.InventoryResponse;
import com.programming.techie.orderservice.dto.OrderLineItemsDto;
import com.programming.techie.orderservice.dto.OrderRequest;
import com.programming.techie.orderservice.model.Order;
import com.programming.techie.orderservice.model.OrderLineItems;
import com.programming.techie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Transactional
    public void placeOrder(OrderRequest orderRequest){
        List<OrderLineItems> orderLineItemsList = orderRequest
                .getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToOrderLineItem)
                .toList();

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(orderLineItemsList);

        List<String> orderSkuCodes = getSkuCodes(orderRequest);

        boolean response = checkInStockProducts(orderSkuCodes);

        if(response){
            orderRepository.save(order);
        }
        else{
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }

    }

    private List<String> getSkuCodes(OrderRequest orderRequest){
        return orderRequest.getOrderLineItemsDtoList().stream()
                .map(OrderLineItemsDto::getSkuCode)
                .toList();
    }

    private boolean checkInStockProducts(List<String> skuCodes){
        InventoryResponse[] response = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        if(response != null){
            return Arrays.stream(response)
                    .allMatch(InventoryResponse::isInStock);
        }
        return false;
    }

    private OrderLineItems mapToOrderLineItem(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        return orderLineItems;
    }
}
