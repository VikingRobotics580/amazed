package frc.robot.subsystems;

import static frc.robot.OI.*;

import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import frc.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.command.Subsystem;
public class DriveSubsystem extends Subsystem {
    WPI_TalonSRX _rFront;
    WPI_TalonSRX _rBack;
    WPI_TalonSRX _lFront;
    WPI_TalonSRX _lBack;

    DifferentialDrive _diffDrive;

    Faults _lFaults;
    Faults _rFaults;

    public DriveSubsystem() {
        _rFront = new WPI_TalonSRX(2);
        _rBack = new WPI_TalonSRX(3);
        _lFront = new WPI_TalonSRX(0);
        _lBack = new WPI_TalonSRX(1);

        _diffDrive = new DifferentialDrive(_lFront, _rFront);
    
        _lFaults = new Faults();
        _rFaults = new Faults();
    }

    public void Driver() {
        String work = "";

        /* get gamepad stick values */
        double forw = -1 * rightJoystick.getRawAxis(1); /* positive is forward */
        double turn = +1 * rightJoystick.getRawAxis(2); /* positive is right */
        boolean btn1 = rightJoystick.getRawButton(1); /* if button is down, print joystick values */

        /* deadband gamepad 10% */
        if (Math.abs(forw) < 0.10) {
            forw = 0;
        }
        if (Math.abs(turn) < 0.10) {
            turn = 0;
        }

        /* drive robot */
        _diffDrive.arcadeDrive(forw, turn);

        /*
        * [2] Make sure Gamepad Forward is positive for FORWARD, and GZ is positive for
        * RIGHT
        */
        work += " GF:" + forw + " GT:" + turn;

        /* get sensor values */
        // double leftPos = _leftFront.GetSelectedSensorPosition(0);
        // double rghtPos = _rghtFront.GetSelectedSensorPosition(0);
        double leftVelUnitsPer100ms = _lFront.getSelectedSensorVelocity(0);
        double rghtVelUnitsPer100ms = _rFront.getSelectedSensorVelocity(0);

        work += " L:" + leftVelUnitsPer100ms + " R:" + rghtVelUnitsPer100ms;

        /*
        * drive motor at least 25%, Talons will auto-detect if sensor is out of phase
        */
        _lFront.getFaults(_lFaults);
        _rFront.getFaults(_rFaults);

        if (_lFaults.SensorOutOfPhase) {
            work += " L sensor is out of phase";
        }
        if (_rFaults.SensorOutOfPhase) {
            work += " R sensor is out of phase";
        }

        /* print to console if btn1 is held down */
        if (btn1) {
            System.out.println(work);
        }
    }

    public void initDefaultCommand() {
        /* factory default values */
        _rFront.configFactoryDefault();
        _rBack.configFactoryDefault();
        _lFront.configFactoryDefault();
        _lBack.configFactoryDefault();

        /* set up followers */
        _rBack.follow(_rFront);
        _lBack.follow(_lFront);

        /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
        _rFront.setInverted(true); // !< Update this
        _lFront.setInverted(false); // !< Update this

        /*
        * set the invert of the followers to match their respective master controllers
        */
        _rBack.setInverted(InvertType.FollowMaster);
        _lBack.setInverted(InvertType.FollowMaster);

        /*
        * [4] adjust sensor phase so sensor moves positive when Talon LEDs are green
        */
        _rFront.setSensorPhase(true);
        _lFront.setSensorPhase(true);

        /*
        * WPI drivetrain classes defaultly assume left and right are opposite. call
        * this so we can apply + to both sides when moving forward. DO NOT CHANGE
        */
        _diffDrive.setRightSideInverted(false);

        setDefaultCommand(new DriveCommand());

    }

}