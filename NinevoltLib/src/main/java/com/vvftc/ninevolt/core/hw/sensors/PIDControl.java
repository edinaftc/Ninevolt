package com.vvftc.ninevolt.core.hw.sensors;

/**
 * Created by Richik SC on 9/26/2017.
 */

public class PIDControl {

  public static class Constants {
    public double p = 0;
    public double i = 0;
    public double d = 0;
    private long t;

    public void setP(double p) {
      this.p = p;
    }

    public void setI(double i) {
      this.i = i;
    }

    public void setD(double d) {
      this.d = d;
    }

    public void setT(long t) {
      this.t = t;
    }

    public long getT() {
      return t;
    }
  }

  public Constants K = new Constants();

  private double integral = 0;


  public PIDControl(double p, long t) {
    this.K.p = p;
    this.K.t = t;
  }

  public PIDControl(double p, double i, long t) {
    this.K.p = p;
    this.K.i = i;
    this.K.t = t;
  }

  public PIDControl(double p, double i, double d, long t) {
    this.K.p = p;
    this.K.i = i;
    this.K.d = d;
    this.K.t = t;
  }

  public double controlPI(double targetValue, double readValue) throws Exception {
    if (this.K.t == 0) {
      throw new Exception("IterationTime in PIDControl.K.t cannot be zero!");
    }
      double error = targetValue - readValue;
      integral += error*K.getT();
      return (K.p*error) + (K.i*integral);
  }

  public void setIntegral(double integral) {
    this.integral = integral;
  }

  public Constants getK() {
    return K;
  }

  public double getIntegral() {
    return integral;
  }

  // You really shouldn't be using this, it's a single use class.
  public void reset() {
    setIntegral(0);
  }
}
