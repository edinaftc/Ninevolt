package com.edinaftc.ninevolt.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Richik SC on 3/3/2017.
 */

public class ExceptionHandling {
  public static void standardExceptionHandling(Exception ex, OpMode opMode) {
    opMode.stop();
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    opMode.telemetry.addData("New " + ex.getClass().toString(), sw.toString());
    opMode.telemetry.update();
  }
}
