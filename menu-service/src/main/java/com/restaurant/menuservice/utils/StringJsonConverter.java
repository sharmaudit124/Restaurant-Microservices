package com.restaurant.menuservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.menuservice.dtos.Discount;
import org.springframework.stereotype.Component;

@Component
public class StringJsonConverter {
    public String getStringifyDiscountDetails(Discount discount) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writeValueAsString(discount);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to process discount details to string type");
        }
    }

    public Discount getDiscountDTO(String discountDetails) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(discountDetails, Discount.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to process discount details to object type");
        }
    }
}
