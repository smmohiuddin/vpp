package com.powerledger.vpp.controller;

import com.powerledger.vpp.model.Battery;
import com.powerledger.vpp.model.BatteryStatistics;
import com.powerledger.vpp.service.BatteryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("batteries")
public class BatteryController {

    private static final Logger logger = LoggerFactory.getLogger(BatteryController.class);

    private final BatteryService batteryService;

    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @PostMapping
    public ResponseEntity<Void> saveBatteries(@RequestBody @Valid List<Battery> batteries) {

        if (batteries.isEmpty()) {
            logger.warn("Received empty battery list");
            throw new IllegalArgumentException("Battery list cannot be empty");
        }

        logger.info("Received {} batteries to save", batteries.size());
        batteryService.saveBatteries(batteries);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<BatteryStatistics> getAllBatteries(@RequestParam String startPostcode, @RequestParam String endPostcode, @RequestParam(required = false) Integer minCapacity, @RequestParam(required = false) Integer maxCapacity) {
        logger.debug("Fetching batteries between postcodes {} and {} with capacity between {} and {}", startPostcode, endPostcode, minCapacity, maxCapacity);

        BatteryStatistics statistics = batteryService.getAllBatteriesByPostcode(startPostcode, endPostcode, minCapacity, maxCapacity);

        logger.info("Found {} batteries with total capacity {} and average capacity {}", statistics.getBatteryNames().size(), statistics.getTotalCapacity(), statistics.getAverageCapacity());

        return ResponseEntity.ok(statistics);
    }
}