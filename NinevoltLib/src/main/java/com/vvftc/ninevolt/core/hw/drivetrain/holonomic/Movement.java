package com.vvftc.ninevolt.core.hw.drivetrain.holonomic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.vvftc.ninevolt.core.hw.Hardware;
import com.vvftc.ninevolt.core.hw.drivetrain.MovementBase;
import com.vvftc.ninevolt.core.hw.sensors.PIDControl;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

/**
 * Created by VVMS FTC teams on 2/23/2017.
 */

public class Movement implements MovementBase {

  private double ppi;

  private boolean isVerbose = false;
  private boolean autoAllowed;
  private OpMode ctx;
  private LinearOpMode ctxl;
  private Telemetry telemetry;
  private Hardware hardware;

  private int newTargetFL;
  private int newTargetFR;
  private int newTargetBL;
  private int newTargetBR;

  public Movement(Hardware hardware, OpMode opMode) {
    this.hardware = hardware;
    this.ctx = opMode;
    this.telemetry = ctx.telemetry;
    autoAllowed = false;
  }

  public Movement(Hardware hardware, LinearOpMode opMode, double ppi) {
    this.hardware = hardware;
    this.ctx = opMode;
    this.telemetry = ctx.telemetry;
    this.ctxl = opMode;
    this.ppi = ppi;
    autoAllowed = true;
  }

  public boolean isVerbose() {
    return isVerbose;
  }

  public void setVerbose(boolean verbose) {
    isVerbose = verbose;
  }

  private void checkAuto() throws Exception {
    if (!autoAllowed) {
      throw new Exception(
          "You must use a LinearOpMode and provide a PPI to be able to use encoders with Ninevolt");
    }
  }

  private int calculateTargetTicks(double targetInches) {
    return (int)Math.round(targetInches * ppi);
  }

  private void setPowerZero() {
    hardware.motorFL.setPower(0);
    hardware.motorFR.setPower(0);
    hardware.motorBL.setPower(0);
    hardware.motorBR.setPower(0);
  }

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
            hardware.motorBR.getCurrentPosition() != 0
        ) {
      ctxl.sleep(500);
    }


    hardware.motorFL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    hardware.motorFR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    hardware.motorBL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    hardware.motorBR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    ctxl.idle();

    newTargetFL = 0;
    newTargetFR = 0;
    newTargetBL = 0;
    newTargetBR = 0;

  }

  @Override
  public void yDrive(double dist) throws Exception {
    resetEncoders();
    if (ctxl.opModeIsActive()) {
      int ticks = calculateTargetTicks(dist);
      hardware.motorFL.setTargetPosition(ticks);
      hardware.motorFR.setTargetPosition(-1*ticks);
      hardware.motorBL.setTargetPosition(ticks);
      hardware.motorBR.setTargetPosition(-1*ticks);

      if(dist > 0)
        directDrive(0, 0.1f, 0);
      else
        directDrive(0, -0.1f, 0);
      // keep looping while we are still active, and there is time left, and both motors are running.
      while (ctxl.opModeIsActive() &&
          (hardware.motorBL.isBusy() && hardware.motorFR.isBusy())) {

        // Display it for the driver.
        telemetry.addData("Path1", "Running to %7d", ticks);
        telemetry.addData("Path2", "Running at %7d :%7d :%7d :%7d",
            hardware.motorFL.getCurrentPosition(),
            hardware.motorFR.getCurrentPosition(),
            hardware.motorBL.getCurrentPosition(),
            hardware.motorBR.getCurrentPosition()
        );
        telemetry.update();


      }
      setPowerZero();
      resetEncoders();
    }

  }

  public void xDrive(double dist) throws Exception {
    resetEncoders();
    if (ctxl.opModeIsActive()) {
      int ticks = calculateTargetTicks(dist);
      hardware.motorFL.setTargetPosition(ticks);
      hardware.motorFR.setTargetPosition(ticks);
      hardware.motorBR.setTargetPosition(-1*ticks);
      hardware.motorBR.setTargetPosition(-1*ticks);

      if(dist > 0) {
        directDrive(0.1f, 0, 0);
      }
      else {
        directDrive(-0.1f, 0, 0);
        telemetry.addData("Else Horizontal", "Negative");
      }
      // keep looping while we are still active, and there is time left, and both motors are running.
      while (ctxl.opModeIsActive() &&
          (hardware.motorBR.isBusy() && hardware.motorFR.isBusy())) {

        // Display it for the driver.
        telemetry.addData("Path1", "Running to %7d", ticks);
        telemetry.addData("Path2", "Running at %7d :%7d :%7d :%7d",
            hardware.motorFL.getCurrentPosition(),
            hardware.motorFR.getCurrentPosition(),
            hardware.motorBR.getCurrentPosition(),
            hardware.motorBR.getCurrentPosition()
        );
        telemetry.update();


      }
      setPowerZero();
      resetEncoders();
    }
  }

  @Override
  public void rotate(double angle) throws Exception {
    telemetry.addData("Ninevolt.Movement", "Sorry, method `rotate` not supported yet");
    telemetry.update();
  }

  @Override
  public void driveUsingRange(double threshold) throws Exception {
    if(autoAllowed) {
      hardware.motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      hardware.motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      hardware.motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      hardware.motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    } else {
      hardware.motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      hardware.motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      hardware.motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      hardware.motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    double cmDist = hardware.rangeSensor.getDistance(DistanceUnit.CM);
    while(cmDist > threshold) {
      directDrive(0.5f);
      cmDist = hardware.rangeSensor.getDistance(DistanceUnit.CM);
      wait(10);
    }
  }

  public void directDrive(float xVal, float yVal, float rotVal) {
    // Holonomic formulas

    float frontLeft = -yVal - xVal - rotVal;
    float frontRight = yVal - xVal - rotVal;
    float backRight = yVal + xVal - rotVal;
    float backLeft = -yVal + xVal - rotVal;

    // Clip the right/left values so that the values never exceed +/- 1
    frontRight = Range.clip(frontRight, -1, 1);
    frontLeft = Range.clip(frontLeft, -1, 1);
    backLeft = Range.clip(backLeft, -1, 1);
    backRight = Range.clip(backRight, -1, 1);
    if (isVerbose()) {
      telemetry.addData("Wheel Value Key", "(Front Left, Front Right, Back Left, Back Right)");
      telemetry.addData("Wheel Values (theoretical)",
          String.format(Locale.US, "(%d, %d, %d, %d)",
              (long) frontLeft,
              (long) frontRight,
              (long) backLeft,
              (long) backRight
          )
      );
      telemetry.update();
    }

    // Write the values to the motors
    hardware.motorFL.setPower(frontLeft);
    hardware.motorFR.setPower(frontRight);
    hardware.motorBL.setPower(backLeft);
    hardware.motorBR.setPower(backRight);
  }

  public void directDrive(float yVal) {
    // Holonomic formulas
    float xVal = 0;
    float rotVal = 0;

    float frontLeft = -yVal - xVal - rotVal;
    float frontRight = yVal - xVal - rotVal;
    float backRight = yVal + xVal - rotVal;
    float backLeft = -yVal + xVal - rotVal;

    // Clip the right/left values so that the values never exceed +/- 1
    frontRight = Range.clip(frontRight, -1, 1);
    frontLeft = Range.clip(frontLeft, -1, 1);
    backLeft = Range.clip(backLeft, -1, 1);
    backRight = Range.clip(backRight, -1, 1);
    if (isVerbose()) {
      telemetry.addData("Wheel Value Key", "(Front Left, Front Right, Back Left, Back Right)");
      telemetry.addData("Wheel Values (theoretical)",
          String.format(Locale.US, "(%d, %d, %d, %d)",
              (long) frontLeft,
              (long) frontRight,
              (long) backLeft,
              (long) backRight
          )
      );
      telemetry.update();
    }

    // Write the values to the motors
    hardware.motorFL.setPower(frontLeft);
    hardware.motorFR.setPower(frontRight);
    hardware.motorBL.setPower(backLeft);
    hardware.motorBR.setPower(backRight);
  }

  @Override
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
      telemetry.addData("currRotation", currentRotation);
      telemetry.update();
      output = pid.controlPI(targetRotation, currentRotation);
      directDrive(0, power, (float)output);
      ctxl.sleep(pid.K.getT());
    }
  }

  @Override
  public void driveUsingGyro(double duration, float power, double targetRotation) throws Exception {
    checkAuto();
    double startTime = ctxl.getRuntime();
    double currentRotation;
    double output;

    // Create new PIDController with K_p of 0.2 and iteration time of 100;
    PIDControl pid = new PIDControl(0.2, 100);

    while (ctxl.getRuntime() < startTime + duration && ctxl.opModeIsActive()) {
      currentRotation = hardware.imu.getAngularOrientation().firstAngle;
      telemetry.addData("currRotation", currentRotation);
      telemetry.update();
      if (currentRotation < (targetRotation + 1) || currentRotation > (targetRotation - 1)) {
        break;
      }
      output = pid.controlPI(targetRotation, currentRotation);
      directDrive(0, power, (float)output);
      ctxl.sleep(pid.K.getT());
    }
  }

}
