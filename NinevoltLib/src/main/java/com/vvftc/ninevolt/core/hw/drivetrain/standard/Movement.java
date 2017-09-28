package com.vvftc.ninevolt.core.hw.drivetrain.standard;

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

  private int newTargetL;
  private int newTargetR;

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
    hardware.motorL.setPower(0);
    hardware.motorR.setPower(0);
  }

  public void resetEncoders() throws Exception {
    checkAuto();
    hardware.motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    hardware.motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    ctxl.idle();

    while (
        hardware.motorL.getCurrentPosition() != 0 &&
            hardware.motorR.getCurrentPosition() != 0
        ) {
      ctxl.sleep(500);
    }


    hardware.motorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    hardware.motorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    ctxl.idle();

    newTargetL = 0;
    newTargetR = 0;

  }

  @Override
  public void yDrive(double dist) throws Exception {
    resetEncoders();
    if (ctxl.opModeIsActive()) {
      int ticks = calculateTargetTicks(dist);
      hardware.motorL.setTargetPosition(ticks);
      hardware.motorR.setTargetPosition(ticks);

      if(dist > 0)
        directDrive(0.1f, 0);
      else
        directDrive(-0.1f, 0);
      // keep looping while we are still active, and there is time left, and both motors are running.
      while (ctxl.opModeIsActive() &&
          (hardware.motorBL.isBusy() && hardware.motorFR.isBusy())) {

        // Display it for the driver.
        telemetry.addData("Path1", "Running to %7d", ticks);
        telemetry.addData("Path2", "Running at %7d :%7d",
            hardware.motorL.getCurrentPosition(),
            hardware.motorR.getCurrentPosition()
        );
        telemetry.update();


      }
      setPowerZero();
      resetEncoders();
    }

  }

  @Override
  public void rotate(double angle) {
    telemetry.addData("Ninevolt.Movement", "Sorry, method `rotate` not supported yet");
    telemetry.update();
  }

//  // Compat method - Ninevolt HotSwap
//  public void drive(double yDist, double angle) {
//
//  }
//
//  // Compat method - Ninevolt HotSwap
//  public void drive(double xDist, double yDist, double angle) {
//
//  }

  public void tankDrive(double lDist, double rDist) {

  }

  public void directTankDrive(double left, double right) {
    hardware.motorL.setPower(Range.clip(left, -1, 1));
    hardware.motorR.setPower(Range.clip(right, -1, 1));
  }

  public void directDrive(double yVal, double rVal) {
    double _rVal = Range.clip(rVal, -1, 1);
    double lMotorPower;
    double rMotorPower;
    if (_rVal < 0) {
      if(isVerbose()) {
        telemetry.addData("rVal", "< 0");
      }
      lMotorPower = yVal * (1 + _rVal);
      rMotorPower = yVal * (1 - _rVal);
    } else if (_rVal > 0) {
      telemetry.addData("rVal", "> 0");
      rMotorPower = yVal * (1 + _rVal);
      lMotorPower = yVal * (1 + _rVal);
    } else {
      lMotorPower = yVal;
      rMotorPower = yVal;
    }
    if(isVerbose()) {
      telemetry.addData("lMotorPower", lMotorPower);
      telemetry.addData("rMotorPower", rMotorPower);
    }
    hardware.motorL.setPower(Range.clip(lMotorPower, -1, 1));
    hardware.motorR.setPower(Range.clip(rMotorPower, -1, 1));
  }

  // Compat method
  public void directDrive(double xVal, double yVal, double rVal) {
    directDrive(yVal, rVal);
  }

  @Override
  public void driveUsingRange(double threshold) throws Exception {
    if(autoAllowed) {
      hardware.motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      hardware.motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    } else {
      hardware.motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      hardware.motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    double cmDist = hardware.rangeSensor.getDistance(DistanceUnit.CM);
    while(cmDist > threshold) {
      directDrive(0.5f, 0);
      cmDist = hardware.rangeSensor.getDistance(DistanceUnit.CM);
      wait(10);
    }
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
