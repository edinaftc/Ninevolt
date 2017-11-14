package com.edinaftc.ninevolt.core.hw.drivetrain;

import com.edinaftc.ninevolt.Config;
import com.edinaftc.ninevolt.Ninevolt;
import com.edinaftc.ninevolt.core.hw.Hardware;
import com.edinaftc.ninevolt.core.hw.sensors.PIDControl;
import com.edinaftc.ninevolt.util.Threshold;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.*;

public abstract class Movement {
  private static final String TAG = "ninevolt.Movement.";
  protected Config config = Ninevolt.getConfig();

  protected double ppi;

  protected boolean autoAllowed;
  protected OpMode ctx;
  protected LinearOpMode ctxl;
  protected Telemetry telemetry;
  protected Hardware hardware;
  protected double rotationDeviation;

  public Movement(Hardware hardware, OpMode opMode) {
    this.hardware = hardware;
    this.ctx = opMode;
    this.telemetry = ctx.telemetry;
    autoAllowed = false;
    rotationDeviation = 0.25;
  }

  public Movement(Hardware hardware, LinearOpMode opMode, double ppi) {
    this.hardware = hardware;
    this.ctx = opMode;
    this.telemetry = ctx.telemetry;
    this.ctxl = opMode;
    this.ppi = ppi;
    autoAllowed = true;
    rotationDeviation = 0.2;
  }

  /**
   * Directly drive the robot with power values.
   * @param xVal Power in the left and right direction. Positive is to the right.
   *             Only supported by holonomic and mecanum drivetrains.
   * @param yVal Power in the forward and backward direction. Positive is forward.
   * @param rotVal Rotating value. Positive is clockwise.
   */
  public abstract void directDrive(float xVal, float yVal, float rotVal);

  /**
   * Directly drive the robot using tank drive control.
   * @param lVal Power of the left wheels. Positive is forward.
   * @param rVal Power to the right wheels. Positive is also forward.
   */
  public abstract void directTankDrive(float lVal, float rVal);

  protected abstract void setTargetX(int ticks);
  protected abstract void setTargetY(int ticks);

  public boolean isVerbose() {
    return config.minLoggingLevel(Config.LoggingLevel.VERBOSE);
  }

  /**
   * Sets the logging output level to 'verbose' or 'recommended'.
   * @deprecated Use {@link Config#setLoggingLevel(Config.LoggingLevel)} instead.
   * @param verbose Whether everything should be logged.
   * @see Config#setLoggingLevel(Config.LoggingLevel)
   */
  @Deprecated
  public void setVerbose(boolean verbose) {
    if (verbose) {
      config.setLoggingLevel(Config.LoggingLevel.VERBOSE);
    } else {
      config.setLoggingLevel(Config.LoggingLevel.RECOMMENDED);
    }
  }

  private void checkAuto() throws Exception {
    if (!autoAllowed) {
      throw new Exception(
          "You must use a LinearOpMode and provide a PPI to be able to use encoders with Ninevolt");
    }
  }

  private int calculateTargetTicks(double targetInches) {
    return (int) Math.round(targetInches * ppi);
  }

  /**
   * Stops all motors.
   */
  public void setPowerZero() {
    hardware.motorFL.setPower(0);
    hardware.motorFR.setPower(0);
    hardware.motorBL.setPower(0);
    hardware.motorBR.setPower(0);
  }

  /**
   * Resets all encoders to zero and then switches them back to <code>RUN_TO_POSITION</code>
   * @throws Exception You are not using Movement with a <code>LinearOpMode</code>
   *                   or have not provided an encoder pulses per inch.
   */
  public void resetEncoders() throws Exception {
    checkAuto();
    hardware.motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    hardware.motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    hardware.motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    hardware.motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    ctxl.idle();

    while (
        hardware.motorFL.getCurrentPosition() != 0 &&
            hardware.motorFR.getCurrentPosition() != 0 &&
            hardware.motorBL.getCurrentPosition() != 0 &&
            hardware.motorBR.getCurrentPosition() != 0 &&
            ctxl.opModeIsActive()
        ) {
      ctxl.sleep(500);
    }

    hardware.motorFL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    hardware.motorFR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    hardware.motorBL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    hardware.motorBR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    ctxl.idle();
  }

  /**
   * Directly drive the robot with power values
   * @param yVal Power in the forward and backward direction. Positive is forward.
   * @param rVal Power in the rotation direction. Positive is clockwise
   */
  public void directDrive(float yVal, float rVal) {
    directDrive(0, yVal, rVal);
  }

  /**
   * Drive the robot in the <i>y</i> direction using encoders for a set distance
   * at a set power.
   * @param dist The distance in inches that the robot should drive. Positive is forward.
   * @param power The power that it should drive at.
   * @throws Exception You are not using a <code>LinearOpMode</code> or have not
   *                   provided an encoder pulses per inch.
   */
  public void yDrive(double dist, float power) throws Exception {
    resetEncoders();
    if (ctxl.opModeIsActive()) {
      int ticks = calculateTargetTicks(dist);
      setTargetY(ticks);

      if (dist > 0)
        directDrive(0, power, 0);
      else
        directDrive(0, -power, 0);
      // keep looping while we are still active, and there is time left, and both motors are running.
      while (ctxl.opModeIsActive() &&
          (hardware.motorBL.isBusy() && hardware.motorFR.isBusy())) {

        if (isVerbose()) {
          // Display it for the driver.
          telemetry.addData(TAG + "yDrive:Target", "Running to %7d", ticks);
          telemetry.addData(TAG + "yDrive:Current", "Running at %7d :%7d :%7d :%7d",
              hardware.motorFL.getCurrentPosition(),
              hardware.motorFR.getCurrentPosition(),
              hardware.motorBL.getCurrentPosition(),
              hardware.motorBR.getCurrentPosition()
          );
          telemetry.update();
        }
      }
      setPowerZero();
      resetEncoders();
    }

  }

  /**
   * Drive the robot in the <i>x</i> direction using encoders for a set distance
   * at a set power.
   * @param dist The distance in inches that the robot should drive. Positive is to the right.
   * @param power The power that it should drive at.
   * @throws Exception You are not using a <code>LinearOpMode</code> or have not
   *                   provided an encoder pulses per inch.
   */
  public void xDrive(double dist, float power) throws Exception {
    resetEncoders();
    if (ctxl.opModeIsActive()) {
      int ticks = calculateTargetTicks(dist);
      setTargetX(ticks);

      if (dist > 0) {
        directDrive(power, 0, 0);
      } else {
        directDrive(-power, 0, 0);
      }
      // keep looping while we are still active, and there is time left, and both motors are running.
      while (ctxl.opModeIsActive() &&
          (hardware.motorBR.isBusy() && hardware.motorFR.isBusy())) {
        if (isVerbose()) {
          // Display it for the driver.
          telemetry.addData(TAG + "xDrive:Target", "Running to %7d", ticks);
          telemetry.addData(TAG + "xDrive:Current", "Running at %7d :%7d :%7d :%7d",
              hardware.motorFL.getCurrentPosition(),
              hardware.motorFR.getCurrentPosition(),
              hardware.motorBR.getCurrentPosition(),
              hardware.motorBR.getCurrentPosition()
          );
          telemetry.update();
        }
      }
      setPowerZero();
      resetEncoders();
    }
  }

  private double targetRotateLogic(double targetRotation) {
    if (targetRotation < -180) {
      return 180 - (targetRotation + 180);
    } else if (targetRotation > 180) {
      return -180 + (targetRotation - 180);
    } else {
      return targetRotation;
    }
  }

  /**
   * Rotate the robot by a provided angle.
   * @param deltaAngle The difference in angle it should rotate in degrees.
   *                   Positive is clockwise.
   * @param power The power it should rotate at.
   */
  public void rotate(double deltaAngle, double power) {
    double currentRotation = hardware.imu.getAngularOrientation().firstAngle;
    double targetRotation = currentRotation - deltaAngle;
    targetRotation = targetRotateLogic(targetRotation);
//    double proportion = 1;
    while (!rotateCondition(targetRotation, currentRotation, (deltaAngle < 0.0)) && opModeIsActive()) {
//      double output = ((targetRotation - currentRotation) / Math.abs(deltaAngle)); // * proportion;
      if (deltaAngle > 0.0) {
        directDrive(0, 0, (float) (power /* * output*/));
      } else if (deltaAngle < 0.0) {
        directDrive(0, 0, (float) (-power /* * output*/));
      } else return;
      currentRotation = hardware.imu.getAngularOrientation().firstAngle;
      if (isVerbose()) {
        telemetry.addData(TAG + "rotate:currentRotation", currentRotation);
        telemetry.addData(TAG + "rotate:targetRotation", targetRotation);
        telemetry.addData(TAG + "rotate:conditionPassed", rotateCondition(targetRotation, currentRotation, (deltaAngle < 0.0)));
        telemetry.update();
      }
      if (ctxl != null) { ctxl.idle(); }
    }
    setPowerZero();

  }

  private boolean rotateCondition(double targetRotation, double currentRotation, boolean counterClockwise) {
    if (counterClockwise) return (currentRotation >= targetRotation);
    else return (currentRotation <= targetRotation);
  }

  /**
   * Drive until the range sensor is within certain distance of an object.
   * @param threshold The distance in centimeters from the object when it should stop.
   * @throws Exception No range sensor was provided.
   */
  public void driveUsingRange(double threshold) throws Exception {
    if (hardware.rangeSensor == null) { throw new Exception("No range sensor was provided!"); }
    setRunUsingEncoders(autoAllowed);
    double cmDist = hardware.rangeSensor.getDistance(DistanceUnit.CM);
    while (cmDist > threshold && opModeIsActive()) {
      directDrive(0.5f, 0);
      cmDist = hardware.rangeSensor.getDistance(DistanceUnit.CM);
      if (isVerbose()) {
        telemetry.addData(TAG + "driveUsingRange:Current Distance", cmDist);
        telemetry.update();
      }
      if (ctxl != null) { ctxl.idle(); }
    }
    setPowerZero();
  }

  public void setRunUsingEncoders(boolean runUsingEncoder) {
    hardware.motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    hardware.motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    hardware.motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    hardware.motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    if (runUsingEncoder) {
      hardware.motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      hardware.motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      hardware.motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      hardware.motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    } else {
      hardware.motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      hardware.motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      hardware.motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      hardware.motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
  }

  public void driveUsingGyro(double duration, float power) throws Exception {
    checkAuto();
    double startTime = ctxl.getRuntime();
    Orientation targetRotationObj = hardware.imu.getAngularOrientation(
        AxesReference.INTRINSIC,
        AxesOrder.ZYX,
        AngleUnit.DEGREES
    );
    double targetRotation = (double) targetRotationObj.firstAngle;
    double currentRotation;
    double output;

    // Create new PIDController with K_p of 0.2 and iteration time of 100;
    PIDControl pid = new PIDControl(0.2, 100);

    while (ctxl.getRuntime() < startTime + duration && ctxl.opModeIsActive()) {
      currentRotation = hardware.imu.getAngularOrientation().firstAngle;
      if (isVerbose()) {
        telemetry.addData(TAG + "driveUsingGyro:currRotation", currentRotation);
        telemetry.update();
      }
      output = pid.controlPI(targetRotation, currentRotation);
      directDrive(0, power, (float) output);
      ctxl.sleep(pid.K.getT());
    }
  }

  public void driveUsingGyro(double duration, float power, double targetRotation) throws Exception {
    checkAuto();
    double startTime = ctxl.getRuntime();
    double currentRotation;
    double output;

    // Create new PIDController with K_p of 0.2 and iteration time of 100;
    PIDControl pid = new PIDControl(0.2, 100);

    while (ctxl.getRuntime() < startTime + duration && ctxl.opModeIsActive()) {
      currentRotation = hardware.imu.getAngularOrientation().firstAngle;
      if (isVerbose()) {
        telemetry.addData(TAG + "driveUsingGyro:currRotation", currentRotation);
        telemetry.update();
      }
      if (currentRotation < (targetRotation + 1) || currentRotation > (targetRotation - 1)) {
        break;
      }
      output = pid.controlPI(targetRotation, currentRotation);
      directDrive(0, power, (float) output);
      ctxl.sleep(pid.K.getT());
    }
  }

  private boolean opModeIsActive() {
    if (ctxl != null) return ctxl.opModeIsActive();
    else return true;
  }
}
