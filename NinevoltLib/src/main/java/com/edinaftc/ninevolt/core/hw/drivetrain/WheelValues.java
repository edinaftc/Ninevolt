package com.edinaftc.ninevolt.core.hw.drivetrain;

public class WheelValues {
  private double fl;
  private double fr;
  private double bl;
  private double br;

  public interface Mapper {
    double run(double val);
  }

  public void setFL(double fl) {
    this.fl = fl;
  }

  public void setFR(double fr) {
    this.fr = fr;
  }

  public void setBL(double bl) {
    this.bl = bl;
  }

  public void setBR(double br) {
    this.br = br;
  }

  public double getFL() {
    return fl;
  }

  public double getFR() {
    return fr;
  }

  public double getBL() {
    return bl;
  }

  public double getBR() {
    return br;
  }

  public void map(Mapper runnable) {
    setFL(runnable.run(fl));
    setFR(runnable.run(fr));
    setBL(runnable.run(bl));
    setBR(runnable.run(br));
  }

  public WheelValues mapTo(Mapper runnable) {
    WheelValues wv = new WheelValues();
    wv.setFL(runnable.run(this.fl));
    wv.setFR(runnable.run(this.fr));
    wv.setBL(runnable.run(this.bl));
    wv.setBR(runnable.run(this.br));
    return wv;
  }

  public void mapFrom(WheelValues wv, Mapper runnable) {
    setFL(runnable.run(wv.getFL()));
    setFR(runnable.run(wv.getFR()));
    setBL(runnable.run(wv.getBL()));
    setBR(runnable.run(wv.getBR()));
  }

  public double max() {
    double maxL = Math.max(fl, fr);
    double maxR = Math.max(bl, br);
    return Math.max(maxL, maxR);
  }

  @Override
  public String toString() {
    return  "FL=" + fl +
            "Fr=" + fr +
            "Bl=" + bl +
            "Br=" + br;
  }
}
