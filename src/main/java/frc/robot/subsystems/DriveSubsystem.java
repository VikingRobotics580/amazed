package frc.robot.subsystems;

import frc.robot.Robot;

import static frc.robot.OI.*;
import static frc.robot.RobotMap.*;

import frc.robot.commands.DriveCommand;
import frc.robot.OI;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class DriveSubsystem extends Subsystem {
    double left;
    double right;
    double position;
    private Talon MotorFour;
    private Talon MotorFive;
    private Talon MotorThree;
    private Talon MotorTwo;
    private int g = 0;

    private DifferentialDrive DriveTrain;
    private SpeedControllerGroup LeftDrive;
    private SpeedControllerGroup RightDrive;

    private double jx;
    private double jy;

    public DriveSubsystem() {
        MotorFour = new Talon(TALON_FOUR);
        MotorFive = new Talon(TALON_FIVE);
        MotorThree = new Talon(TALON_THREE);
        MotorTwo = new Talon(TALON_TWO);

        LeftDrive = new SpeedControllerGroup(MotorFive,MotorFour);
        RightDrive = new SpeedControllerGroup(MotorTwo,MotorThree);

        DriveTrain = new DifferentialDrive(LeftDrive,RightDrive);
        DriveTrain.setSafetyEnabled(false);
    }

    public void Driver() {
        jx = rightJoystick.getX();
        jy = rightJoystick.getY();
        right = (-jy) - (jx);
        left = (-jy) + (jx);
        position = java.lang.Math.abs(left);

        if (position < java.lang.Math.abs(right)) {
        position = java.lang.Math.abs(right);
        }

        if (position > 1) {
        left = left/position;
        right = right/position;
        }
        DriveTrain.tankDrive(left, right);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }

}