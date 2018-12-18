package com.edinaftc.ninevolt.core.hw.drivetrain;

public class WheelValues {
  private double fl;
  private double fr;
  private double bl;
  private double br;

  public interface Mapper {
    double apply(double val);
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

  public void map(Mapper mapper) {
    setFL(mapper.apply(fl));
    setFR(mapper.apply(fr));
    setBL(mapper.apply(bl));
    setBR(mapper.apply(br));
  }

  public WheelValues mapTo(Mapper mapper) {
    WheelValues wv = new WheelValues();
    wv.setFL(mapper.apply(this.fl));
    wv.setFR(mapper.apply(this.fr));
    wv.setBL(mapper.apply(this.bl));
    wv.setBR(mapper.apply(this.br));
    return wv;
  }

  public void mapFrom(WheelValues wv, Mapper mapper) {
    setFL(mapper.apply(wv.getFL()));
    setFR(mapper.apply(wv.getFR()));
    setBL(mapper.apply(wv.getBL()));
    setBR(mapper.apply(wv.getBR()));
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
