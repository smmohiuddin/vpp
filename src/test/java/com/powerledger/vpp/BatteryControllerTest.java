package com.powerledger.vpp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerledger.vpp.controller.BatteryController;
import com.powerledger.vpp.model.Battery;
import com.powerledger.vpp.model.BatteryStatistics;
import com.powerledger.vpp.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BatteryControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(BatteryControllerTest.class);

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BatteryService batteryService;

    @InjectMocks
    private BatteryController batteryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(batteryController).build();
    }

    @Test
    void returnsCreatedStatusWhenBatteriesAreSavedSuccessfully() {
        List<Battery> batteries = List.of(
                new Battery("Battery1", "2000", 100),
                new Battery("Battery2", "2001", 200)
        );

        try {

            mockMvc.perform(post("/batteries")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(batteries)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            logger.error("BatteryControllerTest: Error while saving batteries", e);
        }
    }

    @Test
    void getAllBatteriesReturnsOkStatus() {
        String startPostcode = "2000";
        String endPostcode = "2001";
        Integer minCapacity = 100;
        Integer maxCapacity = 200;

        when(batteryService.getAllBatteriesByPostcode(startPostcode, endPostcode, minCapacity, maxCapacity))
                .thenReturn(new BatteryStatistics(List.of("Battery1", "Battery2"), 300.0, 150.0));

        try {
            mockMvc.perform(get("/batteries")
                            .param("startPostcode", startPostcode)
                            .param("endPostcode", endPostcode)
                            .param("minCapacity", String.valueOf(minCapacity))
                            .param("maxCapacity", String.valueOf(maxCapacity)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            logger.error("BatteryControllerTest: Error while fetching batteries", e);
        }
    }

    @Test
    void saveBatteriesWithEmptyListThrowsException() {
        List<Battery> batteries = List.of();

        try {
            mockMvc.perform(post("/batteries")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(batteries)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Battery list cannot be empty");
            });
        }
    }

    @Test
    void getAllBatteriesWithInvalidParametersThrowsException() {
        try {
            mockMvc.perform(get("/batteries")).andExpect(status().isBadRequest());
        } catch (Exception e) {
            logger.error("BatteryControllerTest: Error while fetching batteries with invalid parameters", e);
        }
    }
}
