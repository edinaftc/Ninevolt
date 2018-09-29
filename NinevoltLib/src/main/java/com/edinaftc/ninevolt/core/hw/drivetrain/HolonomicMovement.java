package com.edinaftc.ninevolt.core.hw.drivetrain;

import com.edinaftc.ninevolt.core.hw.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;

/**
 * The HolonomicMovement class contains movement methods for an omni-swerve
 * drivetrain.
 * @since       2018-02-15
 */

public class HolonomicMovement extends Movement {

  public HolonomicMovement(Hardware hardware, OpMode opMode) throws Exception {
    super(hardware, opMode);
    hardware.setMotorMode(Hardware.MotorMode.HOLONOMIC);
    hardware.updateMotorConfig();
  }

  public HolonomicMovement(Hardware hardware, LinearOpMode opMode, double ppi) throws Exception {
    super(hardware, opMode, ppi);
    hardware.setMotorMode(Hardware.MotorMode.HOLONOMIC);
    hardware.updateMotorConfig();
  }

  @Override
  public void directDrive(float xVal, float yVal, float rotVal) {
    double startTime = ctx.getRuntime();

    // Holonomic formulas
    values.setFL(-yVal - xVal - rotVal);
    values.setFR( yVal - xVal - rotVal);
    values.setBR( yVal + xVal - rotVal);
    values.setBL(-yVal + xVal - rotVal);

    /*  Clip the right/left values so that the values never exceed +/- 1, but
        keeping proportions */
    scaleWheelValues();

    // Show power information to user
    logPowerInfo(startTime);

    // Write the values to the motors
    hardware.setMotorPowers(values);
  }

  @Override
  protected void setTargetX(int ticks) {
    hardware.motorFL.setTargetPosition(ticks);
    hardware.motorFR.setTargetPosition(ticks);
    hardware.motorBL.setTargetPosition(-1 * ticks);
    hardware.motorBR.setTargetPosition(-1 * ticks);
  }

  @Override
  protected void setTargetY(int ticks) {
    hardware.motorFL.setTargetPosition(ticks);
    hardware.motorFR.setTargetPosition(-1 * ticks);
    hardware.motorBL.setTargetPosition(ticks);
    hardware.motorBR.setTargetPosition(-1 * ticks);
  }

  @Override
  public void directTankDrive(float lVal, float rVal) {
    hardware.motorFL.setPower(Range.clip(-lVal, -1, 1));
    hardware.motorBL.setPower(Range.clip(-lVal, -1, 1));
    hardware.motorFR.setPower(Range.clip(rVal, -1, 1));
    hardware.motorBR.setPower(Range.clip(rVal, -1, 1));
  }

}
