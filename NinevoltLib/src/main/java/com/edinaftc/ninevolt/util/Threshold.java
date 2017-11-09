package com.edinaftc.ninevolt.util;

import com.qualcomm.robotcore.util.Range;

public class Threshold {
  public static class Bounds {
    private double lowerBound;
    private double upperBound;

    public Bounds(double lowerBound, double upperBound) {
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
    }

    public void setUpperBound(double upperBound) {
      this.upperBound = upperBound;
    }

    public void setLowerBound(double lowerBound) {
      this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
      return upperBound;
    }

    public double getLowerBound() {
      return lowerBound;
    }

    public boolean within(double value) {
      return (value > lowerBound && value < upperBound);
    }

    public boolean within(int value) {
      return within((double) value);
    }
  }
  public static Bounds threshold(double target, double tolerance) {
    tolerance = Range.clip(tolerance, 0, 1);
    double range = tolerance * target;
    return new Bounds(target - range, target + range);
  }

  public static Bounds threshold(int target, double tolerance) {
    tolerance = Range.clip(tolerance, 0, 1);
    double range = tolerance * target;
    return new Bounds(target - range, target + range);
  }

  public static boolean withinThreshold(double value, double target, double tolerance) {
    Bounds range = threshold(target, tolerance);
    return range.within(value);
  }

  public static boolean withinDeviation(double value, double target, double deviation) {
    if (value > target - deviation && value < target + deviation) return true;
    else return false;
  }
}
