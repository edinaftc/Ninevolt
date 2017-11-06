package com.edinaftc.ninevolt.util;

import com.qualcomm.robotcore.util.Range;

public class Threshold {
    public static double[] threshold(double target, double tolerance) {
        tolerance = Range.clip(tolerance, 0, 1);
        double range = tolerance * target;
        return new double[] {target - range, target + range};
    }

    public static double[] threshold(int target, double tolerance) {
        tolerance = Range.clip(tolerance, 0, 1);
        double range = tolerance * target;
        return new double[] {target - range, target + range};
    }

    public static boolean withinThreshold(double value, double target, double tolerance) {
        double[] range = threshold(target, tolerance);
        if (value > range[0] && value < range[1]) {
            return true;
        }
        return false;
    }
}
