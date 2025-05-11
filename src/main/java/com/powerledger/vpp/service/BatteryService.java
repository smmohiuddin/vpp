package com.powerledger.vpp.service;

import com.google.common.collect.Lists;
import com.powerledger.vpp.model.Battery;
import com.powerledger.vpp.model.BatteryStatistics;
import com.powerledger.vpp.repository.BatteryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatteryService {

    private static final Logger logger = LoggerFactory.getLogger(BatteryService.class);

    private static final int BATCH_SIZE = 100;

    private final BatteryRepository batteryRepository;

    public BatteryService(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    @Transactional
    public void saveBatteries(List<Battery> batteries) {
        logger.info("Processing batch of {} batteries", batteries.size());

        // Process in batches to avoid memory issues and improve performance
        List<List<Battery>> batches = Lists.partition(batteries, BATCH_SIZE);

        batches.parallelStream().forEach(batch -> {
            logger.debug("Processing batch of {} batteries", batch.size());
            batteryRepository.saveAllAndFlush(batch);
        });

        logger.info("Saved total {} batteries", batteries.size());
    }

    public BatteryStatistics getAllBatteriesByPostcode(String startPostcode, String endPostcode, Integer minCapacity, Integer maxCapacity) {

        List<Battery> batteries = batteryRepository.findByPostcodeBetweenAndCapacityOrderByNameASC(startPostcode, endPostcode, minCapacity, maxCapacity);

        logger.info("Found {} batteries", batteries.size());

        List<String> names = this.getBatteryNames(batteries);
        double totalCapacity = this.calculateTotalCapacity(batteries);
        double avgWatt = this.calculateAverageCapacity(totalCapacity, batteries.size());

        return new BatteryStatistics(names, totalCapacity, avgWatt);
    }

    private List<String> getBatteryNames(List<Battery> batteries) {
        return batteries.stream()
                .map(Battery::getName)
                .toList();
    }

    private double calculateTotalCapacity(List<Battery> batteries) {
        return batteries.stream()
                .mapToDouble(Battery::getCapacity)
                .sum();
    }

    private double calculateAverageCapacity(double totalCapacity, int size) {
        return size > 0 ? Math.round(totalCapacity / size * 100.0) / 100.0 : 0; // round up to 2 decimal point
    }
}
