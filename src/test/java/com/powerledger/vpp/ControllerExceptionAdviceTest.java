package com.powerledger.vpp;

import com.powerledger.vpp.controller.ControllerExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerExceptionAdviceTest {

    @InjectMocks
    private ControllerExceptionAdvice controllerExceptionAdvice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void returnsBadRequestForIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<String> response = controllerExceptionAdvice.handleInvalidRequestInputException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"error\":\"Invalid argument\"}", response.getBody());
    }

    @Test
    void returnsBadRequestForMissingServletRequestParameterException() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param", "String");

        ResponseEntity<String> response = controllerExceptionAdvice.handleInvalidRequestInputException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"error\":\"Required request parameter 'param' for method parameter type String is not present\"}", response.getBody());
    }

    @Test
    void returnsInternalServerErrorForGenericException() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<String> response = controllerExceptionAdvice.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred. Please try again later.", response.getBody());
    }
}
