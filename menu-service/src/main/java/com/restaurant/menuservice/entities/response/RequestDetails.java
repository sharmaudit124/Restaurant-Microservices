package com.restaurant.menuservice.entities.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestDetails {
    private String method;
    private String url;
    private Map<String,String> headers;
}
