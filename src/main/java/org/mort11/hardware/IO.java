package org.mort11.hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import org.mort11.util.Constants;

import static org.mort11.util.Constants.*;

/**
 * Contains objects that correlate to hardware on the robot.
 *
 * @author Benny Mirisola
 * @author Seven Kurt
 * @author Joshua Ciffer
 * @author Frankie Alfano
 * @version 02/14/2018
 */
public class IO {

    public static Compressor compressor;
    private static TalonSRX fourbarTalon;
    private static TalonSRX firstStageElevatorTalonMaster, firstStageElevatorTalonFollower;
    private static TalonSRX secondStageElevatorTalon;
    private static VictorSPX intakeRollerVictorRight, intakeRollerVictorLeft;
    private static TalonSRX leftMaster;
    private static TalonSRX leftSlaveMiddle;
    private static TalonSRX leftSlaveBack;
    private static TalonSRX rightMaster;
    private static TalonSRX rightSlaveMiddle;
    private static TalonSRX rightSlaveBack;
    private static DigitalInput intakeLimitSwitchLeft, intakeLimitSwitchRight;
    private static DigitalInput firstStageElevatorLimitSwitchBottom, firstStageElevatorLimitSwitchTop;
    private static DigitalInput secondStageElevatorLimitSwitchBottom, secondStageElevatorLimitSwitchTop;
    private static DigitalInput actuatorLimitSwitchTop, actuatorLimitSwitchBottom;
    private static AnalogInput potentiometerInput;
    // LEDs input and output.
    private static DigitalOutput redLED;
    private static DigitalOutput greenLED;
    private static DigitalOutput blueLED;
    private static Potentiometer potentiometer;
    private static AHRS ahrs;
    /**
     * Double Solenoids.
     */
    private static DoubleSolenoid transmission;
    private static DoubleSolenoid intakePiston;
    private static DoubleSolenoid verticalShifterPiston;

    public static void init() {
        fourbarTalon = new TalonSRX(Constants.ARM_TALON);

        firstStageElevatorTalonMaster = new TalonSRX(Constants.FIRST_STAGE_ELEVATOR_TALON_MASTER);
        firstStageElevatorTalonFollower = new TalonSRX(Constants.FIRST_STAGE_ELEVATOR_TALON_FOLLOWER);
        firstStageElevatorTalonFollower.setInverted(true);

        secondStageElevatorTalon = new TalonSRX(Constants.SECOND_STAGE_ELEVATOR_TALON);

        leftMaster = new TalonSRX(Constants.DRIVETRAIN_LEFT_MASTER);
        leftSlaveMiddle = new TalonSRX(Constants.DRIVETRAIN_LEFT_SLAVE_MIDDLE);
        leftSlaveBack = new TalonSRX((Constants.DRIVETRAIN_LEFT_SLAVE_BACK));

        rightMaster = new TalonSRX(Constants.DRIVETRAIN_RIGHT_MASTER);
        rightSlaveMiddle = new TalonSRX(Constants.DRIVETRAIN_RIGHT_SLAVE_MIDDLE);
        rightSlaveBack = new TalonSRX(Constants.DRIVETRAIN_RIGHT_SLAVE_BACK);

        leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        leftMaster.selectProfileSlot(0, 0);
        rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        rightMaster.selectProfileSlot(0, 0);
        rightMaster.setSensorPhase(true);

        firstStageElevatorTalonMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
        firstStageElevatorTalonMaster.selectProfileSlot(0, 0);
        firstStageElevatorTalonMaster.setSensorPhase(false);
        //firstStageElevatorTalonFollower.setInverted(true);
        // rightMaster.config_kP(0, 0, 0);

        // transmission = new DoubleSolenoid(Constants.PCM_ID, Constants.TRANSMISSION_SHIFT_LOW, Constants.TRANSMISSION_SHIFT_HIGH);

        intakeRollerVictorRight = new VictorSPX(Constants.INTAKE_ROLLER_VICTOR_RIGHT);
        intakeRollerVictorLeft = new VictorSPX(Constants.INTAKE_ROLLER_VICTOR_LEFT);

        intakeLimitSwitchLeft = new DigitalInput(Constants.INTAKE_LIMIT_SWITCH_LEFT);
        intakeLimitSwitchRight = new DigitalInput(Constants.INTAKE_LIMIT_SWITCH_RIGHT);

        firstStageElevatorLimitSwitchBottom = new DigitalInput(Constants.FIRST_STAGE_ELEVATOR_LIMIT_SWITCH_BOTTOM);
        firstStageElevatorLimitSwitchTop = new DigitalInput(Constants.FIRST_STAGE_ELEVATOR_LIMIT_SWITCH_TOP);

        secondStageElevatorLimitSwitchBottom = new DigitalInput(Constants.SECOND_STAGE_ELEVATOR_LIMIT_SWITCH_BOTTOM);
        secondStageElevatorLimitSwitchTop = new DigitalInput(Constants.SECOND_STAGE_ELEVATOR_LIMIT_SWITCH_TOP);

        actuatorLimitSwitchTop = new DigitalInput(Constants.ARM_LIMIT_SWITCH_TOP);
        actuatorLimitSwitchBottom = new DigitalInput(Constants.ARM_LIMIT_SWITCH_BOTTOM);

        redLED = new DigitalOutput(Constants.DIO_RED_LED);
        greenLED = new DigitalOutput(Constants.DIO_GREEN_LED);
        blueLED = new DigitalOutput(Constants.DIO_BLUE_LED);

        initLimitSwitchNames();

        //potentiometerInput = new AnalogInput(Constants.POTENTIOMETER_INPUT);
        //potentiometer = new AnalogPotentiometer(potentiometerInput, 180, 0);

        intakePiston = new DoubleSolenoid(Constants.PCM_ID, Constants.INTAKE_PISTON_IN, Constants.INTAKE_PISTON_OUT);
        intakePiston.setName("intake");

        verticalShifterPiston = new DoubleSolenoid(Constants.PCM_ID, Constants.VERTICAL_SHIFTER_PISTON_UP, Constants.VERTICAL_SHIFTER_PISTON_DOWN);
        verticalShifterPiston.setName("VerticalShifterPiston");

        compressor = new Compressor();
        compressor.start();

        ahrs = new AHRS(SPI.Port.kMXP);

        // Keeps LEDs turned off at the beginning.
        redLED.set(LED_RED_OFF);        // Off
        greenLED.set(LED_GREEN_OFF);    // Off
        blueLED.set(LED_BLUE_OFF);        // Off

    }

    public static void initLimitSwitchNames() {

        intakeLimitSwitchLeft.setName("Left intake Limit Switch");
        intakeLimitSwitchRight.setName("Right intake Limit Switch");

        firstStageElevatorLimitSwitchBottom.setName("Bottom First Stage Elevator Limit Switch");
        firstStageElevatorLimitSwitchTop.setName("Top First Stage Elevator Limit Switch");

        secondStageElevatorLimitSwitchBottom.setName("Bottom Second Stage Elevator Limit Switch");
        secondStageElevatorLimitSwitchTop.setName("Top Second Stage Elevator Limit Switch");

        actuatorLimitSwitchBottom.setName("FourBar Bottom");
        actuatorLimitSwitchTop.setName("Fourbar Top");

        redLED.setName("Red");
        greenLED.setName("Green");
        blueLED.setName("Blue");

    }

    /**
     * Stops all motors and disables all solenoids.
     */
    public static void seize() {
        leftMaster.set(Constants.CONTROL_MODE, 0);
        leftSlaveMiddle.set(Constants.FOLLOWER, 0);
        leftSlaveBack.set(Constants.FOLLOWER, 0);
        rightMaster.set(Constants.CONTROL_MODE, 0);
        rightSlaveMiddle.set(Constants.FOLLOWER, 0);
        rightSlaveBack.set(Constants.FOLLOWER, 0);
    }

    public static TalonSRX getFourbarTalon() {
        return fourbarTalon;
    }

    public static TalonSRX getFirstStageElevatorTalonMaster() {
        return firstStageElevatorTalonMaster;
    }

    public static TalonSRX getFirstStageElevatorTalonFollower() {
        return firstStageElevatorTalonFollower;
    }

    public static TalonSRX getSecondStageElevatorTalon() {
        return secondStageElevatorTalon;
    }

    public static VictorSPX getIntakeRollerVictorRight() {
        return intakeRollerVictorRight;
    }

    public static VictorSPX getIntakeRollerVictorLeft() {
        return intakeRollerVictorLeft;
    }

    public static DigitalInput getIntakeLimitSwitchLeft() {
        return intakeLimitSwitchLeft;
    }

    public static DigitalInput getIntakeLimitSwitchRight() {
        return intakeLimitSwitchRight;
    }

    public static DigitalInput getFirstStageElevatorLimitSwitchBottom() {
        return firstStageElevatorLimitSwitchBottom;
    }

    public static DigitalInput getFirstStageElevatorLimitSwitchTop() {
        return firstStageElevatorLimitSwitchTop;
    }

    public static DigitalInput getSecondStageElevatorLimitSwitchBottom() {
        return secondStageElevatorLimitSwitchBottom;
    }

    public static DigitalInput getSecondStageElevatorLimitSwitchTop() {
        return secondStageElevatorLimitSwitchTop;
    }

    public static DigitalInput getActuatorLimitSwitchTop() {
        return actuatorLimitSwitchTop;
    }

    public static DigitalInput getActuatorLimitSwitchBottom() {
        return actuatorLimitSwitchBottom;
    }

    public static AnalogInput getPotentiometerInput() {
        return potentiometerInput;
    }

    public static Potentiometer getPotentiometer() {
        return potentiometer;
    }

    public static DoubleSolenoid getIntakePiston() {
        return intakePiston;
    }

    public static TalonSRX getLeftMaster() {
        return leftMaster;
    }

    public static TalonSRX getLeftSlaveMiddle() {
        return leftSlaveMiddle;
    }

    public static TalonSRX getLeftSlaveBack() {
        return leftSlaveBack;
    }

    public static TalonSRX getRightMaster() {
        return rightMaster;
    }

    public static TalonSRX getRightSlaveMiddle() {
        return rightSlaveMiddle;
    }

    public static TalonSRX getRightSlaveBack() {
        return rightSlaveBack;
    }

    public static DoubleSolenoid getTransmission() {
        return transmission;
    }

    public static DoubleSolenoid getVerticalShifterPiston() {
        return verticalShifterPiston;
    }

    /**
     * @return NavX
     */
    public static AHRS getAHRS() {
        return ahrs;
    }

    /**
     * @return redLED digital output object.
     */
    public static DigitalOutput getRedLED() {
        return redLED;
    }

    /**
     * @return greenLED digital output object.
     */
    public static DigitalOutput getGreenLED() {
        return greenLED;
    }

    /**
     * @return blueLED digital output object.
     */
    public static DigitalOutput getBlueLED() {
        return blueLED;
    }

}