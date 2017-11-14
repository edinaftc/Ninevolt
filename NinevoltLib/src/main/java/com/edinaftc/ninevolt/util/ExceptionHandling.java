package com.edinaftc.ninevolt.util;

import com.edinaftc.ninevolt.Config;
import com.edinaftc.ninevolt.Ninevolt;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A utility class for various exception handling methods for FTC
 */

public class ExceptionHandling {

  /**
   * A standard exception handling method that stops the OpMode and logs the
   * stack trace to telemetry.
   * @param ex      The exception to handle
   * @param opMode  The context OpMode that this is being called from, usually
   *                <code>this</code>
   */
  public static void standardExceptionHandling(Exception ex, OpMode opMode) {
    try {
      opMode.stop();
      if (Ninevolt.getConfig().minLoggingLevel(Config.LoggingLevel.RECOMMENDED)) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.close();
        sw.close();
        opMode.telemetry.addData("New " + ex.getClass().toString(), sw.toString());
        opMode.telemetry.update();
      }
    } catch (IOException e) {
      opMode.stop();
      opMode.telemetry.addData("Ninevolt.ExceptionHandling Exception",
          "IOException by StringWriter");
      opMode.telemetry.update();
    }
  }
}
