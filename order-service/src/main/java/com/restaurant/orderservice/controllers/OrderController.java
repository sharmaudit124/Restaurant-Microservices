package com.restaurant.orderservice.controllers;

import com.restaurant.orderservice.dto.OrderDetails;
import com.restaurant.orderservice.services.OrderPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderPublisher orderPublisher;

    @PostMapping("/create")
    public ResponseEntity<OrderDetails> createOrder(@RequestBody OrderDetails orderDetails) {
        return new ResponseEntity<>(orderPublisher.sendEventsToTopic(orderDetails), HttpStatus.OK);
    }

}
