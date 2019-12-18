package frc.robot.subsystems;

import static frc.robot.OI.*;

import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import frc.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.command.Subsystem;
public class DriveSubsystem extends Subsystem {
    WPI_TalonSRX _rghtFront;
    WPI_TalonSRX _rghtFollower;
    WPI_TalonSRX _leftFront;
    WPI_TalonSRX _leftFollower;

    DifferentialDrive _diffDrive;

    Faults _faults_L;
    Faults _faults_R;

    public DriveSubsystem() {
        _rghtFront = new WPI_TalonSRX(2);
        _rghtFollower = new WPI_TalonSRX(3);
        _leftFront = new WPI_TalonSRX(0);
        _leftFollower = new WPI_TalonSRX(1);

        _diffDrive = new DifferentialDrive(_leftFront, _rghtFront);
    
        _faults_L = new Faults();
        _faults_R = new Faults();
    }

    public void Driver() {
        String work = "";

        /* get gamepad stick values */
        double forw = -1 * rightJoystick.getRawAxis(1); /* positive is forward */
        double turn = +1 * rightJoystick.getRawAxis(2); /* positive is right */
        boolean btn1 = rightJoystick.getRawButton(1); /* is button is down, print joystick values */

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
        double leftVelUnitsPer100ms = _leftFront.getSelectedSensorVelocity(0);
        double rghtVelUnitsPer100ms = _rghtFront.getSelectedSensorVelocity(0);

        work += " L:" + leftVelUnitsPer100ms + " R:" + rghtVelUnitsPer100ms;

        /*
        * drive motor at least 25%, Talons will auto-detect if sensor is out of phase
        */
        _leftFront.getFaults(_faults_L);
        _rghtFront.getFaults(_faults_R);

        if (_faults_L.SensorOutOfPhase) {
            work += " L sensor is out of phase";
        }
        if (_faults_R.SensorOutOfPhase) {
            work += " R sensor is out of phase";
        }

        /* print to console if btn1 is held down */
        if (btn1) {
            System.out.println(work);
        }
    }

    public void initDefaultCommand() {
        /* factory default values */
        _rghtFront.configFactoryDefault();
        _rghtFollower.configFactoryDefault();
        _leftFront.configFactoryDefault();
        _leftFollower.configFactoryDefault();

        /* set up followers */
        _rghtFollower.follow(_rghtFront);
        _leftFollower.follow(_leftFront);

        /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
        _rghtFront.setInverted(true); // !< Update this
        _leftFront.setInverted(false); // !< Update this

        /*
        * set the invert of the followers to match their respective master controllers
        */
        _rghtFollower.setInverted(InvertType.FollowMaster);
        _leftFollower.setInverted(InvertType.FollowMaster);

        /*
        * [4] adjust sensor phase so sensor moves positive when Talon LEDs are green
        */
        _rghtFront.setSensorPhase(true);
        _leftFront.setSensorPhase(true);

        /*
        * WPI drivetrain classes defaultly assume left and right are opposite. call
        * this so we can apply + to both sides when moving forward. DO NOT CHANGE
        */
        _diffDrive.setRightSideInverted(false);

        setDefaultCommand(new DriveCommand());

    }

}