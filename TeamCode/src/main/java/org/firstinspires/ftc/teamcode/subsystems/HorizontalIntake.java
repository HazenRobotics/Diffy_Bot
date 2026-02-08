package org.firstinspires.ftc.teamcode.subsystems;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HorizontalIntake {
    public static double MAX_POWER = 0.5;
    public static double TICKS_PER_INCH = 1000;
    public static double EXTENSION_SENSITIVITY = 0.01;
    public static double SHIFT_SENSITIVITY = 0.01;

    //Internal variables
    private DcMotorEx left, right;
    //State variables
    private double targetExtension, targetSide;


    HorizontalIntake(HardwareMap hw){
        this(hw, "leftHorizontal","rightHorizontal");
    }

    HorizontalIntake(HardwareMap hw, String leftName, String rightName){
        left = hw.get(DcMotorEx.class, leftName);
        right = hw.get(DcMotorEx.class,rightName);

        //Utilize RUN_TO_POSITION
        left.setTargetPosition(left.getCurrentPosition());
        right.setTargetPosition(right.getCurrentPosition());
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setPower(MAX_POWER);
        right.setPower(MAX_POWER);
    }

    /**
     * TeleOp implementation to allow easy input via joysticks/triggers
     * @param extension Extension power [-1,1]. (Negative being retracting)
     * @param sideShift Shift power [-1,1]. (Negative shifting the opposite direction)
     */
    public void power(double extension, double sideShift){
        //Assume motors spin together --> extend/retract
        //Spin opposite --> side-to-side
        targetExtension += extension * EXTENSION_SENSITIVITY;
        targetSide += sideShift * EXTENSION_SENSITIVITY;
        update();
    }


    /**
     * Set the desired position
     * @param extensionInch Extension distance of slides in inches
     * @param sideInch Side to side shift distance from the center.
     */
    public void setPosition(double extensionInch, double sideInch){
        targetExtension = TICKS_PER_INCH * extensionInch;
        targetSide = TICKS_PER_INCH * sideInch;
        update();
    }

    /**
     * Updates the target positions for the left and right positions
     * Without this function, nothing should move.
     */
    public void update(){
        left.setTargetPosition((int) (targetExtension + targetSide));
        right.setTargetPosition((int) (targetExtension - targetSide));
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    public String toString(){
        return String.format("Target Extension: %d\n" +
                             "Actual Extension: %d\n" +
                             "Target Shift: %d\n" +
                             "Actual Shift: %d\n\n" +
                             "Left Motor Pos: %d\n" +
                             "Right Motor Pos: %d\n",
                            (int) targetExtension,
                (left.getCurrentPosition() + right.getCurrentPosition())/2,
                            (int) targetSide,
                (left.getCurrentPosition() - right.getCurrentPosition())/2,
                            left.getCurrentPosition(),
                            right.getCurrentPosition()
        );
    }
}
