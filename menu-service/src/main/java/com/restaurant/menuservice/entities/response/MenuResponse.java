package com.restaurant.menuservice.entities.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuResponse<T> {
    private T response;
    private ErrorDetails error;
    private RequestDetails request;
    private LocalDateTime timeStamp;
}
