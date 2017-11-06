package com.edinaftc.ninevolt.core.hw;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.edinaftc.ninevolt.util.DcMotorPair;

/**
 * Created by VVMS FTC teams on 2/23/2017.
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

  // Lot to do, decide, getters and setters or public members (usually bad practice but used by
  // HW PushBot FTC sample class)

  // TODO: Write hardware class

  public Hardware() {
    dfZeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
  }

  public void init() throws Exception {
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
    if (motorFL != null) {
      motorFL.setZeroPowerBehavior(dfZeroPowerBehavior);
      motorFR.setZeroPowerBehavior(dfZeroPowerBehavior);
    }
    if (motorBL != null) {
      motorBL.setZeroPowerBehavior(dfZeroPowerBehavior);
      motorBR.setZeroPowerBehavior(dfZeroPowerBehavior);
    }
    if (imu != null) {
      if(imuParams != null) {
        imu.initialize(imuParams);
      } else {
        throw new Exception("YOU SUCK! YOU DID NOT INCLUDE PARAMETERS! THIS SHOULD NOT HAPPEN " +
            "UNLESS YOU ARE DOING THINGS MANUALLY WHICH YOU SHOULD NOT BE DOING ANYWAY! USE HB!");
      }
    }
  }

  public void setMotorMode(MotorMode motorMode) {
    this.motorMode = motorMode;
  }

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

  public void setImuParams(BNO055IMU.Parameters imuParams) {
    this.imuParams = imuParams;
  }

  public void setDfZeroPowerBehavior(DcMotor.ZeroPowerBehavior dfZeroPowerBehavior) {
    this.dfZeroPowerBehavior = dfZeroPowerBehavior;
  }
}
