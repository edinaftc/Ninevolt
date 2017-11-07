package com.edinaftc.ninevolt.core.hw.drivetrain;

public class WheelValues {
  private float fl;
  private float fr;
  private float bl;
  private float br;

  public interface Mapper {
    float run(float val);
  }

  public void setFL(float fl) {
    this.fl = fl;
  }

  public void setFR(float fr) {
    this.fr = fr;
  }

  public void setBL(float bl) {
    this.bl = bl;
  }

  public void setBR(float br) {
    this.br = br;
  }

  public float getFL() {
    return fl;
  }

  public float getFR() {
    return fr;
  }

  public float getBL() {
    return bl;
  }

  public float getBR() {
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

  public float max() {
    float maxL = Math.max(fl, fr);
    float maxR = Math.max(bl, br);
    return Math.max(maxL, maxR);
  }
}
