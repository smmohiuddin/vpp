package com.powerledger.vpp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class, HandlerMethodValidationException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<String> handleInvalidRequestInputException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        logger.warn("Illegal argument: {}", ex.getMessage());
        return this.prepareErrorResponse(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again later.");
    }

    private ResponseEntity<String> prepareErrorResponse(Map<String, String> errors) {
        try {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectMapper().writeValueAsString(errors));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
