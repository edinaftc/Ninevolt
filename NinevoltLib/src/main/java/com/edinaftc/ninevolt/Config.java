package com.edinaftc.ninevolt;

/**
 * Configuration class for the Ninevolt library. Using this out of the box will
 * use default values. It is recommended you extend this class and customize it.
 */
public class Config {

  /**
   * Represents the telemetry output logging level of the Ninevolt library.
   */
  public enum LoggingLevel {
    /**
     * Silent - setting this logging level will not log anything to telemetry
     * from the library.
     */
    SILENT(0),

    /**
     * Recommended - it is recommended you use this logging level in competition.
     * Doing so will result in less memory usage and not overwhelming the operators.
     */
    RECOMMENDED(5),

    /**
     * Verbose is the maximum logging level. It is not recommended you use this
     * in competition as the memory usage can cause stability issues.
     * It will log everything such as individual position and sensor information
     * during drive commands. This mode is great for debugging.
     */
    VERBOSE(10);

    private final int level;
    LoggingLevel(int levelNum) {
      this.level = levelNum;
    }

    /**
     * Returns the integer level of a logging level.
     * This is useful for comparing logging levels.
     * @return level - an integer from 1-10 representing the current level.
     */
    public int getLevel() {
      return level;
    }
  }

  private LoggingLevel loggingLevel;

  /**
   * Creates a new configuration with its logging level set to @link{LoggingLevel.VERBOSE}.
   */
  public Config() {
    this.loggingLevel = LoggingLevel.VERBOSE;
  }

  /**
   * Gets the current logging level of this configuration.
   * @return The current logging level of this configuration.
   */
  public LoggingLevel getLoggingLevel() {
    return loggingLevel;
  }

  /**
   * Checks if the current logging level of the config is at least a minimum.
   * @param minimum The minimum logging level to check the current against.
   * @return True if the current logging level is greater than the minimum.
   */
  public boolean minLoggingLevel(LoggingLevel minimum) {
    return loggingLevel.getLevel() >= minimum.getLevel();
  }

  /**
   * Sets the current logging level of this configuration.
   * @param loggingLevel The logging level to set the config's logging level to.
   */
  public void setLoggingLevel(LoggingLevel loggingLevel) {
    this.loggingLevel = loggingLevel;
  }
}
