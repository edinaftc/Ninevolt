package com.edinaftc.ninevolt;

public class Config {
  public enum LoggingLevel {
    SILENT, RECOMMENDED, VERBOSE
  }

  private LoggingLevel loggingLevel;

  public Config() {
    this.loggingLevel = LoggingLevel.VERBOSE;
  }

  public LoggingLevel getLoggingLevel() {
    return loggingLevel;
  }

  public void setLoggingLevel(LoggingLevel loggingLevel) {
    this.loggingLevel = loggingLevel;
  }
}
