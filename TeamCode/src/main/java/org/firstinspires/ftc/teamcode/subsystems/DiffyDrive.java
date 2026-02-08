package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.DirectionalVector;

public class DiffyDrive {


    private DiffyDrivePod left, right;


    public DiffyDrive(HardwareMap hw){
        left = new DiffyDrivePod(hw, "topLeft", "bottomLeft", "leftEncoder");
        right = new DiffyDrivePod(hw, "topRight", "bottomRight", "rightEncoder");
    }

    /**
     * Core driving function for controlling swerve drive
     * @param forward - left joystick y axis (up-and-down) input [-1, 1]
     * @param strafe - left joystick x axis (left and right) input [-1, 1]
     * @param rotate - right joystick x axis (left and right) input [-1, 1]
     */
    public void drive(double forward, double strafe, double rotate){
        double input_threshold = 0.1; //"Close enough to zero"
        //3 primary drive conditions to consider:
        //1. Only drive (rotate = 0 && forward + strafe != 0)
        //2. Only rotate (strafe + forward = 0 && forward + strafe)
        //3. Both drive and rotate (rightStick != 0 && strafe + forward != 0)
        //Technically there is all = 0, but this is trivial and should be handled by default
        //By doing nothing
        double mag = Math.sqrt(forward * forward + strafe + strafe);
//        //If forward/strafe and rotate are all non-zero:
//        if(mag > input_threshold && rotate > input_threshold){
//            //Do some special combination;
//            //Add the vectors together option
//            left.drive(strafe, forward+rotate);
//            right.drive(strafe, forward-rotate);
//        }
//        //Otherwise, if only forward/strafe are non-zero:
//        else if (mag > input_threshold){
//            //Simply drive the pods in parallel
//            left.drive(strafe, forward);
//            right.drive(strafe,forward);
//        }
//        //Otherwise if only rotate is non-zero:
//        else if (rotate > input_threshold){
//            //Align the pods parallel so they face forward/backward
//            //Assuming this is perpendicular to the Center of Mass/Rotation,
//            //This should provide the most optimal rotation vector based on
//            //the rotation magnitude.
//            left.drive(0, rotate);
//            right.drive(0, -rotate);
//        }
        //Otherwise forward/strafe and rotate are all 0
//        else{
//            //Do nothing
//        }


        //Simplified resulting code setup:
        if(mag > input_threshold || rotate >input_threshold){
            left.drive(strafe, forward+rotate);
            right.drive(strafe, forward-rotate);
        }
    }

    @NonNull
    public String toString(){
        return String.format("Left Pod Status: %s\n"+
                             "Right Pod Status: %s\n",
                            left.toString(),
                            right.toString());
    }


}
