package com.edinaftc.ninevolt.core.hw.drivetrain;

/**
 * Created by Richik SC on 2/23/2017.
 */

@Deprecated
public interface MovementBase {
  void yDrive(double dist) throws Exception;
  void rotate(double angle) throws Exception;
  void driveUsingRange(double threshold) throws Exception;
  void driveUsingGyro(double duration, float power) throws Exception;
  void driveUsingGyro(double duration, float power, double targetRotation) throws Exception;
}
