package org.firstinspires.ftc.teamcode.subsystems;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VerticalDeposit {
    //Constants
    public static final double MAX_POWER = 0.5;

    public static double TICKS_PER_INCH = 1000;
    public static double TICKS_PER_ROTATION = 1000;
    public static double EXTENSION_SENSITIVITY = 0.01;
    public static double ROTATION_SENSITIVITY = 0.01;

    //Internal Variables
    private DcMotorEx left, right;

    private double targetExtension, targetRotate;

    private int extensionOffset, rotateOffset;

    VerticalDeposit(HardwareMap hw, String leftName, String rightName){
        left = hw.get(DcMotorEx.class, leftName);
        right = hw.get(DcMotorEx.class, rightName);

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
     * @param rotation Shift power [-1,1]. (Negative shifting the opposite direction)
     */
    public void power(double extension, double rotation){
        //Assume motors spin together --> extend/retract
        //Spin opposite --> side-to-side
        targetExtension += extension * EXTENSION_SENSITIVITY;
        targetRotate += rotation * ROTATION_SENSITIVITY;
        update();
    }


    /**
     * Set the desired position
     * @param extensionInch Extension distance of slides in inches
     * @param rotateAngle Side to side shift distance from the center.
     */
    public void setPosition(double extensionInch, double rotateAngle){
        targetExtension = TICKS_PER_INCH * extensionInch;
        targetRotate = TICKS_PER_ROTATION * rotateAngle;
        update();
    }

    /**
     * To be used/called when a magnetic limit switch is activated
     * This will reset the shifting offset in assumption that the
     * current position of the deposit is centered
     */
    public void resetShiftOffset(){
        rotateOffset = (left.getCurrentPosition() - right.getCurrentPosition()) / 2;
    }

    /**
     * To be used/called when a limit switch is activated
     * This will reset the extension offset in the assumption that the
     * current position
     */
    public void resetExtensionOffset(){
        extensionOffset = (left.getCurrentPosition() + right.getCurrentPosition()) / 2;
    }

    /**
     * Updates the target positions for the left and right positions
     * Without this function, nothing should move.
     */
    public void update(){
        left.setTargetPosition((int) (targetExtension + targetRotate) - extensionOffset - rotateOffset);
        right.setTargetPosition((int) (targetExtension - targetRotate) - extensionOffset + rotateOffset);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    public String toString(){
        return String.format("Target Extension: %d\n" +
                        "Actual Extension: %d\n" +
                        "Target Rotation: %d\n" +
                        "Actual Rotation: %d\n\n" +
                        "Left Motor Pos: %d\n" +
                        "Right Motor Pos: %d\n" +
                        "Extension Offset: %d\n " +
                        "Rotation Offset: %d\n",
                (int) targetExtension,
                (left.getCurrentPosition() + right.getCurrentPosition())/2,
                (int) targetRotate,
                (left.getCurrentPosition() - right.getCurrentPosition())/2,
                left.getCurrentPosition(),
                right.getCurrentPosition(),
                extensionOffset,
                rotateOffset
        );
    }
}
