package com.edinaftc.ninevolt;

public class Ninevolt {
  private static Config config = new Config();

  public static void setConfig(Config config) {
    Ninevolt.config = config;
  }

  public static Config getConfig() {
    return config;
  }
}
