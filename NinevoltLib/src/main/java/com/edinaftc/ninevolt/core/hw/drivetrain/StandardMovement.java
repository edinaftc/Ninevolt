package com.edinaftc.ninevolt.core.hw.drivetrain;

import com.edinaftc.ninevolt.core.hw.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;

/**
 * The StandardMovement class contains movement methods for a standard wheeled
 * drivetrain.
 * @since       2018-02-15
 */

public class StandardMovement extends Movement {

  public StandardMovement(Hardware hardware, OpMode opMode, Hardware.MotorMode motorCount) throws Exception {
    super(hardware, opMode);
    if (motorCount != Hardware.MotorMode.FOUR_MOTORS &&
        motorCount != Hardware.MotorMode.TWO_MOTORS) {
      throw new Exception("Use MotorMode TWO_ or FOUR_MOTORS with StandardMovement!");
    }
    hardware.setMotorMode(motorCount);
    hardware.updateMotorConfig();
  }

  public StandardMovement(Hardware hardware, LinearOpMode opMode, Hardware.MotorMode motorCount, double ppi) throws Exception {
    super(hardware, opMode, ppi);
    if (motorCount != Hardware.MotorMode.FOUR_MOTORS &&
        motorCount != Hardware.MotorMode.TWO_MOTORS) {
      throw new Exception("Use MotorMode TWO_ or FOUR_MOTORS with StandardMovement!");
    }
    hardware.setMotorMode(motorCount);
    hardware.updateMotorConfig();
  }

  @Override
  public void directDrive(float xVal, float yVal, float rotVal) {
    double startTime = ctx.getRuntime();

    // Mecanum formulas
    values.setFL(yVal + rotVal);
    values.setFR(yVal - rotVal);

    /*  Clip the right/left values so that the values never exceed +/- 1, but
        keeping proportions */
    valuesAbs.mapFrom(values, new WheelValues.Mapper() {
      @Override
      public float run(float val) {
        return Math.abs(val);
      }
    });

    final float maxAbs = valuesAbs.max();
    if (maxAbs > 1) {
      values.map(new WheelValues.Mapper() {
        @Override
        public float run(float val) {
          return val / maxAbs;
        }
      });
    }

    // Show power information to user
    logPowerInfo(startTime);

    // Write the values to the motors
    hardware.motorL.setPower(values.getFL());
    hardware.motorR.setPower(values.getFR());
  }

  @Override
  protected void logPowerInfo(double startTime) {
    if (isVerbose()) {
      telemetry.addData("Wheel Value Key", "(Left, Right)");
      telemetry.addData("Wheel Values (theoretical)",
          String.format(Locale.US, "(%.2f, %.2f, %.2f, %.2f)",
              values.getFL(),
              values.getFR()
          )
      );

      if(hardware.motorFL.getMode() == DcMotor.RunMode.RUN_USING_ENCODER &&
          hardware.motorFR.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
        telemetry.addData("Wheel TPS",
            String.format(Locale.US, "(%d, %d, %d, %d)",
                (long) (hardware.motorFL.getCurrentPosition() / (ctx.getRuntime() - startTime)),
                (long) (hardware.motorFR.getCurrentPosition() / (ctx.getRuntime() - startTime))
            )
        );
      }
      telemetry.update();
    }
  }

  @Override
  protected void setTargetX(int ticks) {
    hardware.motorL.setTargetPosition(0);
    hardware.motorR.setTargetPosition(0);
  }

  @Override
  protected void setTargetY(int ticks) {
    hardware.motorL.setTargetPosition(ticks);
    hardware.motorR.setTargetPosition(ticks);
  }

  @Override
  public void directTankDrive(float lVal, float rVal) {
    hardware.motorL.setPower(Range.clip(lVal, -1, 1));
    hardware.motorR.setPower(Range.clip(rVal, -1, 1));
  }
}
