package com.edinaftc.ninevolt;

public class Ninevolt {
  private static Ninevolt instance;
  public static Ninevolt getInstance() {
    return instance;
  }

  private Config config;

  public void setConfig(Config config) {
    this.config = config;
  }

  public Config getConfig() {
    return config;
  }
}
