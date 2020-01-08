package frc.robot.subsystems;

import static frc.robot.OI.*;

import frc.robot.commands.LimelightCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

public class LimelightSubsystem extends Subsystem {
    
    public LimelightSubsystem() {

    }


    public void initDefaultCommand() {

        setDefaultCommand(new LimelightCommand());

    }

}