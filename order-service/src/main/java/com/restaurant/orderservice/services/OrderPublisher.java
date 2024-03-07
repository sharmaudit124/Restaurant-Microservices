package com.restaurant.orderservice.services;

import com.restaurant.orderservice.dto.OrderDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class OrderPublisher {

    @Autowired
    private KafkaTemplate<String, Object> template;

    public OrderDetails sendEventsToTopic(OrderDetails orderDetails) {
        try {
            CompletableFuture<SendResult<String, Object>> future = template.send("order-chef", orderDetails);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Sending order details to kafka-> {}", orderDetails);
                    log.info("{} {} {}",orderDetails.getOrderId(), orderDetails.getOrderDetails(), orderDetails.getOrderStatus());
                    //save to database
                } else {
                    log.error("Unable to send message due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            log.error("ERROR" + ex.getMessage());
        }

        return orderDetails;
    }
}
