package com.edinaftc.ninevolt.core.hw.drivetrain;

import com.edinaftc.ninevolt.core.hw.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;

/**
 * The <code>HolonomicMovement</code> class contains movement methods for an
 * omni-swerve drivetrain.
 * @since 1.0.4-alpha
 */
public class HolonomicMovement extends Movement {

  private WheelValues values;

  public HolonomicMovement(Hardware hardware, OpMode opMode) throws Exception {
    super(hardware, opMode);
    values = new WheelValues();
    hardware.setMotorMode(Hardware.MotorMode.HOLONOMIC);
    hardware.updateMotorConfig();
  }

  public HolonomicMovement(Hardware hardware, LinearOpMode opMode, double ppi) throws Exception {
    super(hardware, opMode, ppi);
    values = new WheelValues();
    hardware.setMotorMode(Hardware.MotorMode.HOLONOMIC);
    hardware.updateMotorConfig();
  }

  @Override
  public void directDrive(float xVal, float yVal, float rotVal) {
    double startTime = ctx.getRuntime();

    // Holonomic formulas
    values.setFL(-yVal - xVal - rotVal);
    values.setFR(yVal - xVal - rotVal);
    values.setBR(yVal + xVal - rotVal);
    values.setBL(-yVal + xVal - rotVal);

    // Clip the right/left values so that the values never exceed +/- 1
    values.map(new WheelValues.Mapper() {
      @Override
      public float run(float val) {
        return Range.clip(val, -1, 1);
      }
    });

    // Show power information to user
    if (isVerbose()) {
      telemetry.addData("Wheel Value Key", "(Front Left, Front Right, Back Left, Back Right)");
      telemetry.addData("Wheel Values (theoretical)",
          String.format(Locale.US, "(%.2f, %.2f, %.2f, %.2f)",
              values.getFL(),
              values.getFR(),
              values.getBL(),
              values.getBR()
          )
      );

      if(hardware.motorFL.getMode() == DcMotor.RunMode.RUN_USING_ENCODER &&
          hardware.motorFR.getMode() == DcMotor.RunMode.RUN_USING_ENCODER &&
          hardware.motorBL.getMode() == DcMotor.RunMode.RUN_USING_ENCODER &&
          hardware.motorBR.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
        telemetry.addData("Wheel TPS",
            String.format(Locale.US, "(%d, %d, %d, %d)",
                (long) (hardware.motorFL.getCurrentPosition() / (ctx.getRuntime() - startTime)),
                (long) (hardware.motorFR.getCurrentPosition() / (ctx.getRuntime() - startTime)),
                (long) (hardware.motorBL.getCurrentPosition() / (ctx.getRuntime() - startTime)),
                (long) (hardware.motorBR.getCurrentPosition() / (ctx.getRuntime() - startTime))
            )
        );
      }
      telemetry.update();
    }

    // Write the values to the motors
    hardware.setMotorPowers(values);
  }

  @Override
  protected void setTargetX(int ticks) {
    hardware.motorFL.setTargetPosition(ticks);
    hardware.motorFR.setTargetPosition(ticks);
    hardware.motorBR.setTargetPosition(-1 * ticks);
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
    values.setFL(-lVal);
    values.setFR(rVal);
    values.setBR(rVal);
    values.setBL(-lVal);

    values.map(new WheelValues.Mapper() {
      @Override
      public float run(float val) {
        return Range.clip(val, -1, 1);
      }
    });

    hardware.setMotorPowers(values);
  }
}
