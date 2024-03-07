package com.restaurant.menuservice.utils;

import com.restaurant.menuservice.entities.response.ErrorDetails;
import com.restaurant.menuservice.entities.response.MenuResponse;
import com.restaurant.menuservice.entities.response.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SuccessResponse {
    public static final ErrorDetails NULL_ERROR_DETAILS = new ErrorDetails();
    private final HttpServletRequest httpServletRequest;

    public SuccessResponse(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public <T> ResponseEntity<MenuResponse<T>> getResponse(T response, HttpStatus status) {
        log.info("preparing response for given details.");
        synchronized (SuccessResponse.class) {
            MenuResponse<T> menuResponse = new MenuResponse<>();
            menuResponse.setError(NULL_ERROR_DETAILS);
            menuResponse.setRequest(getRequestDetails());
            menuResponse.setResponse(response);
            menuResponse.setTimeStamp(LocalDateTime.now());

            return new ResponseEntity<>(menuResponse, status);
        }
    }

    private RequestDetails getRequestDetails() {
        log.info("fetching current request details");
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setMethod(httpServletRequest.getMethod());
        requestDetails.setUrl(httpServletRequest.getRequestURL().toString());

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        requestDetails.setHeaders(headers);

        return requestDetails;
    }
}
