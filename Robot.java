/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.*;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;

public class Robot extends TimedRobot {

  //Motor Controllers
  private MotorLayout robo= new MotorLayout(10);

  //Controllers
  private final Joystick chainStick = new Joystick(0);
  private final Joystick driveStick = new Joystick(1);

  private final JoystickButton pulleyUpButton = new JoystickButton(chainStick, 3);
  private final JoystickButton pulleyDownButton = new JoystickButton(chainStick, 4);

  private final JoystickButton liftUpButton = new JoystickButton(chainStick, 5);
  private final JoystickButton liftDownButton = new JoystickButton(chainStick, 6);

  private final JoystickButton cargoIn = new JoystickButton(chainStick, 11);
  private final JoystickButton cargoOut = new JoystickButton(chainStick, 12);

  private final Servo cargoServo = new Servo(0);


  private final JoystickButton polarityButton = new JoystickButton(driveStick, 2);
  boolean isPolaritySet = false;



  //input areas
  private final Timer m_timer = new Timer();
  private final int ultraPort = 0;
  private final double conversion = 0.5;//voltage to cm
  public double currentDistance = 0;
  private double x;
	private double y;
	private double stickY;
  private double stickX;
  private double stickSense;

  //Varius ints and timers
  private AnalogInput ultrasonic = new AnalogInput(ultraPort);

  private DigitalInput limitSwitch = new DigitalInput(0);


  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {

    m_timer.reset();
    m_timer.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    stickY = driveStick.getY();
    stickX = driveStick.getX();
      
    stickSense = driveStick.getRawAxis(3) + 2;
      
    //right side
    robo.getController(1).set((stickY+stickX) / stickSense);
    robo.getController(2).set((-stickY+stickX) / stickSense);
    //left side
    robo.getController(3).set((stickY+stickX) / stickSense);
    robo.getController(4).set((-stickY+stickX) / stickSense);
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
    x = 0;
    y = 0;
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  
  @Override
  public void teleopPeriodic() {
    stickY = driveStick.getY();
    stickX = driveStick.getX();
      
    stickSense = driveStick.getRawAxis(3) + 2;

    if (!isPolaritySet) {
        
      //right side
      robo.getController(1).set((stickY+stickX) / stickSense);
      robo.getController(2).set((stickY+stickX) / stickSense);
      //left side
      robo.getController(3).set((-stickY+stickX) / stickSense);
      robo.getController(4).set((-stickY+stickX) / stickSense);


      if (polarityButton.get()) { isPolaritySet = true; }

      //Move chain mechanism with stick
      //robo.getController(9).set(chainStick.getY());
      if (liftUpButton.get()) {
        robo.getController(9).set(0.4);
      }
      else if (liftDownButton.get()) {
        robo.getController(9).set(-0.4);
      }
      else {
        robo.getController(9).set(0.0);
      }

      //Move ramp in and out with buttons if the limit switch is not being pressed
      
      if (pulleyDownButton.get()) {
        robo.getController(7).set(-1.0);
      }
      else if (pulleyDownButton.get()) {
        robo.getController(7).set(1.0);
      }
      else {
        robo.getController(7).set(0.0);
      }

    }

    else if (isPolaritySet) {

      //right side
      robo.getController(1).set((-stickY+stickX) / stickSense);
      robo.getController(2).set((-stickY+stickX) / stickSense);
      //left side
      robo.getController(3).set((stickY+stickX) / stickSense);
      robo.getController(4).set((stickY+stickX) / stickSense);


      if (polarityButton.get()) { isPolaritySet = false; }

      //Move chain mechanism with stick
      //robo.getController(9).set(chainStick.getY());
      if (liftUpButton.get()) {
        robo.getController(9).set(-0.4);
      }
      else if (liftDownButton.get()) {
        robo.getController(9).set(0.4);
      }
      else {
        robo.getController(9).set(0.0);
      }

      //Move ramp in and out with buttons if the limit switch is not being pressed
      
      if (pulleyDownButton.get()) {
        robo.getController(7).set(1.0);
      }
      else if (pulleyDownButton.get()) {
        robo.getController(7).set(-1.0);
      }
      else {
        robo.getController(7).set(0.0);
      }
    }

    

    if (cargoIn.get()) {
      cargoServo.set(1.0);
    }
    else if (cargoOut.get()) {
      cargoServo.set(-1.0);
    }
    else {
      cargoServo.set(0.0);
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

  }
}

