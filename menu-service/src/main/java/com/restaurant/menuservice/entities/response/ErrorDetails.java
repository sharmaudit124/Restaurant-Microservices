package com.restaurant.menuservice.entities.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDetails {
    private int code;
    private String message;
    private String details;
    private List<String> causes;
    private List<String> troubleshooting;

}
