package com.restaurant.menuservice.exceptions;

import com.restaurant.menuservice.constants.ErrorConstants;
import com.restaurant.menuservice.entities.response.ErrorDetails;
import com.restaurant.menuservice.entities.response.MenuResponse;
import com.restaurant.menuservice.entities.response.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final HttpServletRequest httpServletRequest;

    @Autowired
    public GlobalExceptionHandler(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @ExceptionHandler({NoElementFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MenuResponse> handleNoElementFoundException(NoElementFoundException e) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.NOT_FOUND, e, e.getData(), ErrorConstants.NOT_FOUND_CAUSES, ErrorConstants.TROUBLESHOOTING_STEPS_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ElementAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<MenuResponse> handleElementAlreadyExistException(ElementAlreadyExistException e) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.CONFLICT, e, e.getData(), ErrorConstants.ERROR_CAUSES_CONFLICT, ErrorConstants.TROUBLESHOOTING_STEPS_CONFLICT), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({CustomServiceException.class, IllegalArgumentException.class, IllegalStateException.class, CustomIllegalException.class, NullPointerException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MenuResponse> handleBadRequestException(Exception e) {
        Object errorData = null;

        if (e instanceof CustomServiceException) {
            errorData = ((CustomServiceException) e).getData();
        } else if (e instanceof CustomIllegalException) {
            errorData = ((CustomIllegalException) e).getData();
        }

        return new ResponseEntity<>(createErrorResponse(HttpStatus.BAD_REQUEST, e, errorData, ErrorConstants.ERROR_CAUSES_BAD_REQUEST, ErrorConstants.TROUBLESHOOTING_STEPS_BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MenuResponse> handleInternalServerError(Exception e) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e, null, ErrorConstants.ERROR_CAUSES_INTERNAL_SERVER_ERROR, ErrorConstants.TROUBLESHOOTING_STEPS_INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private MenuResponse createErrorResponse(HttpStatus status, Exception e, Object data, List<String> causes, List<String> troubleshooting) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(e.getMessage());
        errorDetails.setCode(status.value());
        errorDetails.setMessage(e.getLocalizedMessage());
        errorDetails.setCauses(causes);
        errorDetails.setTroubleshooting(troubleshooting);

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
        MenuResponse menuResponse = new MenuResponse();
        menuResponse.setError(errorDetails);
        menuResponse.setRequest(requestDetails);
        if (data != null) {
            menuResponse.setResponse(data);
        } else {
            menuResponse.setResponse(null);
        }
        menuResponse.setTimeStamp(LocalDateTime.now());

        return menuResponse;
    }
}
