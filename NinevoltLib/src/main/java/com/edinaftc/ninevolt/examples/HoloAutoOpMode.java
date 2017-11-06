package com.edinaftc.ninevolt.examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.edinaftc.ninevolt.core.hw.Hardware;
import com.edinaftc.ninevolt.core.hw.HardwareBuilder;
import com.edinaftc.ninevolt.core.hw.drivetrain.holonomic.Movement;
import com.edinaftc.ninevolt.util.ExceptionHandling;

/**
 * Created by Richik SC on 3/3/2017.
 * Duplicate this file into teamcode to run.
 */

@Autonomous(name = "Holonomic Square", group = "Ninevolt Sample Autonomous")
@Disabled
public class HoloAutoOpMode extends LinearOpMode {

  private static final int PULSES_PER_MOTOR_REV = 28;
  private static final double DRIVE_GEAR_REDUCTION = 40.0;
  private static final double WHEEL_DIAMETER_INCHES = 4;
  private static final double     PULSES_PER_INCH         = (PULSES_PER_MOTOR_REV *
      DRIVE_GEAR_REDUCTION) /
      (WHEEL_DIAMETER_INCHES * 3.1415);

  private Hardware hardware;
  private Movement movement;

  private void custom_init() throws Exception {
    HardwareBuilder hb = new HardwareBuilder(hardwareMap);
    hb.setMotorConfig(Hardware.MotorMode.HOLONOMIC, DcMotor.Direction.FORWARD)
      .addMotorFL("motor_fl").addMotorFR("motor_fr")
      .addMotorBL("motor_bl").addMotorBR("motor_br");
    this.hardware = hb.build();
    hb = null;
    hardware.init();
    movement = new Movement(hardware, this, PULSES_PER_INCH);
    movement.setVerbose(true);
  }

  @Override
  public void runOpMode() throws InterruptedException {
    try {
      custom_init();
      waitForStart();
      if(opModeIsActive()) {
        movement.yDrive(48);
        movement.xDrive(-48);
        movement.yDrive(-48);
        movement.xDrive(48);
      }
    } catch (Exception ex) {
      ExceptionHandling.standardExceptionHandling(ex, this);
      if (ex instanceof InterruptedException) {
        throw (InterruptedException)ex;
      }
    }

  }
}
