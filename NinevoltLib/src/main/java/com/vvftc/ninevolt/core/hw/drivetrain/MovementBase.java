package com.vvftc.ninevolt.core.hw.drivetrain;

/**
 * Created by Richik SC on 2/23/2017.
 */

public interface MovementBase {
  void yDrive(double dist) throws Exception; // TODO: Name temporary, TBD.
  void rotate(double angle) throws Exception;
  void driveUsingRange(double threshold) throws Exception;
  void driveUsingGyro(double duration, float power) throws Exception;
  void driveUsingGyro(double duration, float power, double targetRotation) throws Exception;
}
