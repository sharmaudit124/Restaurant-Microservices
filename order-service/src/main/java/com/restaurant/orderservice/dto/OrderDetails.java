package com.restaurant.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetails {
    private String orderId;
    private String orderDetails;
    private OrderStatus orderStatus;

}
