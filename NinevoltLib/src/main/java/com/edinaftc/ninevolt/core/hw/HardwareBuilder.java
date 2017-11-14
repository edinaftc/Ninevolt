package com.edinaftc.ninevolt.core.hw;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Builder class to create hardware step-by-step.
 * <a href="https://sourcemaking.com/design_patterns/builder">Follows the
 * builder pattern</a> and allows you to daisy-chain methods.
 */

public class HardwareBuilder {

  private HardwareMap hardwareMap;
  private Hardware building;

  public HardwareBuilder(HardwareMap hardwareMap) {
    this.hardwareMap = hardwareMap;
    building = new Hardware();
  }

  @Deprecated
  public HardwareBuilder setMotorConfig(Hardware.MotorMode motorMode,
                                        DcMotor.Direction motorDirection) {
    building.setMotorMode(motorMode);
    building.setMotorDirection(motorDirection);
    return this;
  }

  public HardwareBuilder setMotorDirection(DcMotor.Direction direction) {
    building.setMotorDirection(direction);
    return this;
  }

  public HardwareBuilder addMotorFL(String hwMapName) throws Exception {
    building.setMotorFL(hardwareMap.dcMotor.get(hwMapName));
    return this;
  }
  public HardwareBuilder addMotorFR(String hwMapName) throws Exception {
    building.setMotorFR(hardwareMap.dcMotor.get(hwMapName));
    return this;
  }
  public HardwareBuilder addMotorBL(String hwMapName) throws Exception {
    building.setMotorBL(hardwareMap.dcMotor.get(hwMapName));
    return this;
  }
  public HardwareBuilder addMotorBR(String hwMapName) throws Exception {
    building.setMotorBR(hardwareMap.dcMotor.get(hwMapName));
    return this;
  }

  public HardwareBuilder addBoschIMU(String hwMapName, BNO055IMU.Parameters parameters) {
    building.setImu(hardwareMap.get(BNO055IMU.class, hwMapName));
    building.setImuParams(parameters);
    return this;
  }

  public HardwareBuilder addMRRangeSensor(String hwMapName) {
    building.setRangeSensor(hardwareMap.get(ModernRoboticsI2cRangeSensor.class, hwMapName));
    return this;
  }

  public Hardware build() {
    return building;
  }
}
