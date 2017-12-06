package com.edinaftc.ninevolt;

public class Config {
  public enum LoggingLevel {
    SILENT(0), RECOMMENDED(5), VERBOSE(10);

    private final int level;
    LoggingLevel(int levelNum) {
      this.level = levelNum;
    }

    public int getLevel() {
      return level;
    }
  }

  private LoggingLevel loggingLevel;

  public Config() {
    this.loggingLevel = LoggingLevel.VERBOSE;
  }

  public LoggingLevel getLoggingLevel() {
    return loggingLevel;
  }

  public boolean minLoggingLevel(LoggingLevel minimum) {
    return loggingLevel.getLevel() >= minimum.getLevel();
  }

  public void setLoggingLevel(LoggingLevel loggingLevel) {
    this.loggingLevel = loggingLevel;
  }
}
