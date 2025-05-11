package com.powerledger.vpp.model;

import java.util.List;

public class BatteryStatistics {
        private List<String> batteryNames;
    private double totalCapacity;
    private double averageCapacity;

    public BatteryStatistics(List<String> batteryNames, double totalWattCapacity, double averageWattCapacity) {
        this.batteryNames = batteryNames;
        this.totalCapacity = totalWattCapacity;
        this.averageCapacity = averageWattCapacity;
    }

    public List<String> getBatteryNames() {
        return batteryNames;
    }

    public void setBatteryNames(List<String> batteryNames) {
        this.batteryNames = batteryNames;
    }

    public double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public double getAverageCapacity() {
        return averageCapacity;
    }

    public void setAverageCapacity(double averageCapacity) {
        this.averageCapacity = averageCapacity;
    }

    @Override
    public String toString() {
        return "Battery Statistics {" +
                "batteryNames=" + batteryNames +
                ", totalCapacity=" + totalCapacity +
                ", averageCapacity=" + averageCapacity +
                '}';
    }
}
