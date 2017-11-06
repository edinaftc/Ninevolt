package com.edinaftc.ninevolt.util;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Richik SC on 2/23/2017.
 */

public class DcMotorPair {
  private String noMotorEx = "Ninevolt: No motors remaining!";
  private DcMotor motor1;
  private DcMotor motor2;
  private boolean isSingleMotor = false;

  public DcMotorPair(DcMotor motor1, DcMotor motor2) throws Exception {
    this.motor1 = motor1;
    this.motor2 = motor2;
    if(motor1 == null ^ motor2 == null) {
      isSingleMotor = true;
    } else {
      throw new Exception(noMotorEx);
    }
  }

  public DcMotorPair(DcMotor motor1) {
    this.motor1 = motor1;
    isSingleMotor = true;
  }

  public DcMotor getMotor1() {
    return motor1;
  }

  public DcMotor getMotor2() {
    return motor2;
  }

  public void setMotor1(DcMotor motor1)throws Exception {
    this.motor1 = motor1;
    if (this.motor1 == null && motor2 != null) {
      isSingleMotor = true;
    } else if (this.motor1 != null && motor2 != null) {
      isSingleMotor = false;
    } else {
      throw new Exception(noMotorEx);
    }

  }

  public void setMotor2(DcMotor motor2) throws Exception {
    this.motor2 = motor2;
    if (motor1 != null && this.motor2 == null) {
      isSingleMotor = true;
    } else if (motor1 != null && this.motor2 != null) {
      isSingleMotor = false;
    } else {
      throw new Exception(noMotorEx);
    }
  }

  public boolean isSingleMotor() {
    return isSingleMotor;
  }


  public void setPower(double power) {
    if(motor1 != null)
      motor1.setPower(power);
    if(motor2 != null)
      motor2.setPower(power);
  }

  public void setDirection(DcMotor.Direction direction) {
    if(motor1 != null)
      motor1.setDirection(direction);
    if(motor2 != null)
      motor2.setDirection(direction);
  }

  public void setMode(DcMotor.RunMode runMode) {
    if(motor1 != null)
      motor1.setMode(runMode);
    if(motor2 != null) {
      motor2.setMode(runMode);
    }
  }

  public void setTargetPosition(int targetPosition) {
    if(motor1 != null)
      motor1.setTargetPosition(targetPosition);
    if(motor2 != null) {
      motor2.setTargetPosition(targetPosition);
    }
  }

  public int getCurrentPosition() throws Exception {
    if(motor1 != null && motor2 != null) {
      return motor1.getCurrentPosition();
    }
    if(motor1 != null)
      return motor1.getCurrentPosition();
    else if(motor2 != null)
      return motor2.getCurrentPosition();
    else
      throw new Exception(noMotorEx);
  }

}
