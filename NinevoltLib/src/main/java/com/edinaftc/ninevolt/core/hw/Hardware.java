package com.edinaftc.ninevolt.core.hw;
import com.edinaftc.ninevolt.core.hw.drivetrain.WheelValues;
import com.edinaftc.ninevolt.util.DcMotorPair;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * A class to represent the hardware of your robot. Contains things such as
 * motors and sensors.
 */

public class Hardware {
  public enum MotorMode {
    TWO_MOTORS, FOUR_MOTORS, HOLONOMIC, MECANUM
  }
  private MotorMode motorMode;
  private DcMotor.Direction motorDirection;
  private DcMotor.ZeroPowerBehavior dfZeroPowerBehavior;


  // Motor hardware members
  public DcMotor motorFL;
  public DcMotor motorFR;
  public DcMotor motorBL;
  public DcMotor motorBR;

  public DcMotorPair motorL;
  public DcMotorPair motorR;

  public ModernRoboticsI2cRangeSensor rangeSensor;
  public BNO055IMU imu;
  public BNO055IMU.Parameters imuParams;

  public Hardware() {
    dfZeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
  }

  public void init() throws Exception {
    setDfZeroPowerBehavior(this.dfZeroPowerBehavior);
    if (imu != null) {
      if(imuParams != null) {
        imu.initialize(imuParams);
      } else {
        throw new Exception("YOU SUCK! YOU DID NOT INCLUDE PARAMETERS! THIS SHOULD NOT HAPPEN " +
            "UNLESS YOU ARE DOING THINGS MANUALLY WHICH YOU SHOULD NOT BE DOING ANYWAY! USE HB!");
      }
    }
  }

  /**
   * Applies updated motor mode and direction to motors.
   * @throws Exception Either <code>motorMode</code> or <code>motorDirection</code>
   * is not defined.
   */
  public void updateMotorConfig() throws Exception {
    if (motorMode == MotorMode.HOLONOMIC) {
      if(motorDirection != null) {
        motorFL.setDirection(motorDirection);
        motorBL.setDirection(motorDirection);
        motorFR.setDirection(motorDirection);
        motorBR.setDirection(motorDirection);
      } else {
        throw new Exception("Must specify motor type");
      }
    } else if (motorMode == MotorMode.MECANUM) {
      if(motorDirection == DcMotor.Direction.FORWARD) {
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorBL.setDirection(DcMotor.Direction.REVERSE);
        motorFR.setDirection(DcMotor.Direction.FORWARD);
        motorBR.setDirection(DcMotor.Direction.FORWARD);
      } else if (motorDirection == DcMotor.Direction.REVERSE) {
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        motorFR.setDirection(DcMotor.Direction.REVERSE);
      } else {
        throw new Exception("Must specify motor type");
      }
    } else if (motorMode == MotorMode.FOUR_MOTORS) {
      if (motorDirection == DcMotor.Direction.FORWARD) {
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        motorFR.setDirection(DcMotor.Direction.REVERSE);
      } else if (motorDirection == DcMotor.Direction.REVERSE) {
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorBL.setDirection(DcMotor.Direction.REVERSE);
        motorFR.setDirection(DcMotor.Direction.FORWARD);
        motorBR.setDirection(DcMotor.Direction.FORWARD);
      } else {
        throw new Exception("Must specify motor type");
      }
      motorL = new DcMotorPair(motorFL, motorBL);
      motorR = new DcMotorPair(motorFR, motorBR);
    } else if(motorMode == MotorMode.TWO_MOTORS) {
      if(motorDirection == DcMotor.Direction.REVERSE) {
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorFR.setDirection(DcMotor.Direction.FORWARD);
      } else if (motorDirection == DcMotor.Direction.FORWARD) {
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorFR.setDirection(DcMotor.Direction.REVERSE);
      } else {
        throw new Exception("Must specify motor type");
      }
      motorL = new DcMotorPair(motorFL);
      motorR = new DcMotorPair(motorFR);
    } else {
      throw new Exception("Must specify motor mode");
    }
  }

  /**
   * Updates motor mode based on chassis type. Please note that it does not
   * apply the updated value to the motors which requires calling the
   * {@link #updateMotorConfig()} method.
   * @param motorMode The chassis type, one of four: <code>TWO_MOTORS</code>,
   *                  <code>FOUR_MOTORS</code>, <code>HOLONOMIC</code>,
   *                  <code>MECANUM</code>
   */
  public void setMotorMode(MotorMode motorMode) {
    this.motorMode = motorMode;
  }

  /**
   * Updates motor direction. Please note that it does not apply the updated
   * value to the motors which requires calling the {@link #updateMotorConfig()}
   * method.
   * @param direction The chassis type, either <code>FORWARD</code>,
   *                  <code>REVERSE</code>
   */
  public void setMotorDirection(DcMotor.Direction direction) {
    this.motorDirection = direction;
  }

  public void setMotorFL(DcMotor motorFL) {
    this.motorFL = motorFL;
  }

  public void setMotorFR(DcMotor motorFR) {
    this.motorFR = motorFR;
  }

  public void setMotorBL(DcMotor motorBL) {
    this.motorBL = motorBL;
  }

  public void setMotorBR(DcMotor motorBR) {
    this.motorBR = motorBR;
  }

  public void setImu(BNO055IMU imu) {
    this.imu = imu;
  }

  public void setRangeSensor(ModernRoboticsI2cRangeSensor rangeSensor) {
    this.rangeSensor = rangeSensor;
  }

  public void setImuParams(BNO055IMU.Parameters imuParams) {
    this.imuParams = imuParams;
  }

  public void setDfZeroPowerBehavior(DcMotor.ZeroPowerBehavior dfZeroPowerBehavior) {
    this.dfZeroPowerBehavior = dfZeroPowerBehavior;
    if (motorFL != null) {
      motorFL.setZeroPowerBehavior(dfZeroPowerBehavior);
      motorFR.setZeroPowerBehavior(dfZeroPowerBehavior);
    }
    if (motorBL != null) {
      motorBL.setZeroPowerBehavior(dfZeroPowerBehavior);
      motorBR.setZeroPowerBehavior(dfZeroPowerBehavior);
    }
  }

  public void setMotorPowers(WheelValues values) {
    if (motorBL != null && motorBR != null &&
        motorMode != MotorMode.TWO_MOTORS) {
      motorFL.setPower(values.getFL());
      motorFR.setPower(values.getFR());
      motorBL.setPower(values.getBL());
      motorBR.setPower(values.getBR());
    }
  }
}
