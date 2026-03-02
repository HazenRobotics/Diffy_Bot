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
    //Assume there will be a limit switch/sensor for resetting offsets to avoid drift
    //State variables
    private double targetExtension, targetSide;

    private int extensionOffset, shiftOffset;


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
        targetSide += sideShift * SHIFT_SENSITIVITY;
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
     * To be used/called when a magnetic limit switch is activated
     * This will reset the shifting offset in assumption that the
     * current position of the deposit is centered
     */
    public void resetShiftOffset(){
        /*Take the current difference of the motors
        To reset, the intake system should be in the center of it's shift
        Thus, the "side" movement should be 0.
        If there the motors claim different, then we need to shift them  so the difference
        becomes 0 */
        shiftOffset = (left.getCurrentPosition() - right.getCurrentPosition()) / 2;
        /*Ex:
        L = 100, R= 200
        Diff = -100
        Offset = Diff / 2 = -50
        L = 100 - Offset = 150
        R = 200 + Offset = 150
        */
    }

    /**
     * To be used/called when a limit switch is activated
     * This will reset the extension offset in the assumption that the
     * current position
     */
    public void resetExtensionOffset(){
        /*
        In this instance, the average position of the motors is 0
        Thus, the current average subtracted from the left and right will be the new "0"
         */
        extensionOffset = (left.getCurrentPosition() + right.getCurrentPosition()) / 2;
        /*
        Ex:
        L = 100, R = 200
        Avg = (L + R) / 2 = 150
        L = 100 - Avg = -50
        R = 200 - Avg = 50
        New Avg = 0
         */
    }

    /**
     * Updates the target positions for the left and right positions
     * Without this function, nothing should move.
     */
    public void update(){
        left.setTargetPosition((int) (targetExtension + targetSide) - extensionOffset - shiftOffset);
        right.setTargetPosition((int) (targetExtension - targetSide) - extensionOffset + shiftOffset);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    public String toString(){
        return String.format("Target Extension: %d\n" +
                             "Actual Extension: %d\n" +
                             "Target Shift: %d\n" +
                             "Actual Shift: %d\n\n" +
                             "Left Motor Pos: %d\n" +
                             "Right Motor Pos: %d\n" +
                            "Extension Offset: %d\n " +
                            "Shift Offset: %d\n",
                            (int) targetExtension,
                (left.getCurrentPosition() + right.getCurrentPosition())/2,
                            (int) targetSide,
                (left.getCurrentPosition() - right.getCurrentPosition())/2,
                            left.getCurrentPosition(),
                            right.getCurrentPosition(),
                            extensionOffset,
                            shiftOffset
        );
    }
}
