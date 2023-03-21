package com.pixel4r.plugin.enderice.fishingoverhaul.fishingoverhaul.utils;

public class MathUtils {
    public static double randDouble(double bound1, double bound2) {
        double min = Math.min(bound1, bound2);
        double max = Math.max(bound1, bound2);
        return min + (Math.random() * (max - min));
    }
}
