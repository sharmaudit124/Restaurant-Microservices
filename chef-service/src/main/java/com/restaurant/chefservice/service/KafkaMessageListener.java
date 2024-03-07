package com.restaurant.chefservice.service;

import com.restaurant.chefservice.dto.OrderDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaMessageListener {

    @KafkaListener(topics = "order-chef", groupId = "oc-group")
    public void consumeEvents(OrderDetails orderDetails) {
        log.info("consumer consume the events. ");
        log.info("{} {} {}", orderDetails.getOrderId(), orderDetails.getOrderDetails(), orderDetails.getOrderStatus());
    }
}
