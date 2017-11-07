package com.edinaftc.ninevolt.core.hw.drivetrain;

import com.edinaftc.ninevolt.core.hw.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class MecanumMovement extends Movement {

  public MecanumMovement(Hardware hardware, OpMode opMode) throws Exception {
    super(hardware, opMode);
    hardware.setMotorMode(Hardware.MotorMode.MECANUM);
    hardware.updateMotorConfig();
  }

  public MecanumMovement(Hardware hardware, LinearOpMode opMode, double ppi) throws Exception {
    super(hardware, opMode, ppi);
    hardware.setMotorMode(Hardware.MotorMode.MECANUM);
    hardware.updateMotorConfig();
  }

  @Override
  public void directDrive(float xVal, float yVal, float rotVal) {

  }

  @Override
  public void setTargetX(int ticks) {

  }

  @Override
  public void setTargetY(int ticks) {

  }
}
