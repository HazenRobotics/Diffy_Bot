package org.firstinspires.ftc.teamcode.utils;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CotEncoder {
    //Constants
    public static double ROTATION_DEGREES = 360;
    public static final double MAX_VOLTAGE = 3.3;

    //State variables
    private AnalogInput encoder;
    private double angularOffset;
    private int inverted; //1 or -1


    public CotEncoder(HardwareMap hw, String name){
        this(hw, name, 0, 1);
    }

    public CotEncoder(HardwareMap hw, String name, double offset, int inverted){
        encoder = hw.analogInput.get(name);
        angularOffset = offset;
        this.inverted = inverted;
    }

    /**
     * Gets the rotational position of the swerve pod
     * @return rotational position in vector form
     */
    public DirectionalVector getPosition(){
        return new DirectionalVector(getAngleRaw());
    }

    public double getVoltage(){
        return encoder.getVoltage();
    }

    public void setAngularOffset(double degrees){
        angularOffset = degrees;
    }

    //Returns the angle output in degrees
    public double getAngleRaw(){
        return ROTATION_DEGREES - ((((getVoltage()/MAX_VOLTAGE) * ROTATION_DEGREES * inverted) + angularOffset + ROTATION_DEGREES) % ROTATION_DEGREES);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    public String toString(){
        return String.format(
                        "Max Voltage: %f\n" +
                        "Angular Offset: %f\n" +
                        "Inverted Multiplier: %d\n" +
                        "Current Voltage: %f\n" +
                        "Calculated Angle: %f"
                        , MAX_VOLTAGE
                        , angularOffset
                        , inverted
                        , getVoltage()
                        , getAngleRaw()
        );
    }
}
