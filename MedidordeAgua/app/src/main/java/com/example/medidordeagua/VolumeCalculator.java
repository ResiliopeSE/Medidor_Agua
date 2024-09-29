package com.example.medidordeagua;

public class VolumeCalculator {

    private static final double PI = 3.141592653589793;

    public static double calculateVolume(double radius, double containerHeight, double distance) {
        // Calculate the water height in the container based on the distance measured
        double waterHeight = containerHeight - distance;

        // Ensure the waterHeight is not negative or exceeding container height
        if (waterHeight < 0) waterHeight = 0;
        if (waterHeight > containerHeight) waterHeight = containerHeight;

        // Calculate the volume in cubic centimeters (cm³)
        double volumeCm3 = PI * Math.pow(radius, 2) * waterHeight;

        // Convert cm³ to milliliters (1 cm³ = 1 ml)
        return volumeCm3; // In milliliters
    }
}
