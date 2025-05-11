package com.powerledger.vpp;

import com.powerledger.vpp.model.Battery;
import com.powerledger.vpp.model.BatteryStatistics;
import com.powerledger.vpp.repository.BatteryRepository;
import com.powerledger.vpp.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BatteryServiceTest {

    @Mock
    private BatteryRepository batteryRepository;

    @InjectMocks
    private BatteryService batteryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBatteriesByPostcode() {
        List<Battery> batteries = List.of(
                new Battery("Battery1", "2000", 100),
                new Battery("Battery2", "2001", 200)
        );

        when(batteryRepository.findByPostcodeBetweenAndCapacityOrderByNameASC("2000", "2001", 50, 300)).thenReturn(batteries);

        BatteryStatistics result = batteryService.getAllBatteriesByPostcode("2000", "2001", 50, 300);

        assertEquals(List.of("Battery1", "Battery2"), result.getBatteryNames());
        assertEquals(300.0, result.getTotalCapacity());
        assertEquals(150.0, result.getAverageCapacity());
    }

    @Test
    void handlesDivisionByZeroWhenNoBatteriesFound() {
        when(batteryRepository.findByPostcodeBetweenAndCapacityOrderByNameASC("5000", "6000", 50, 300))
                .thenReturn(List.of());

        BatteryStatistics result = batteryService.getAllBatteriesByPostcode("5000", "6000", 50, 300);

        assertEquals(0.0, result.getAverageCapacity());
    }

    @Test
    void savesBatteriesSuccessfully() {
        List<Battery> batteries = List.of(
                new Battery("Battery1", "2000", 100),
                new Battery("Battery2", "2001", 200)
        );

        when(batteryRepository.saveAllAndFlush(batteries)).thenReturn(batteries);

        batteryService.saveBatteries(batteries);

        verify(batteryRepository, times(1)).saveAllAndFlush(batteries);
    }
}
