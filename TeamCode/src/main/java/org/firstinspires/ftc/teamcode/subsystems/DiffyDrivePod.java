package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.CotEncoder;
import org.firstinspires.ftc.teamcode.utils.DirectionalVector;
import org.firstinspires.ftc.teamcode.utils.PIDController;

public class DiffyDrivePod{
    private DcMotorEx top, bottom;
    private CotEncoder encoder;
    private PIDController pid;

    //Either 1 or -1 to invert direction of power motors
    private int invertState;

    private DirectionalVector targetAngle;

    double DRIVE_THRESHOLD = 0.09, POWER_THRESHOLD = 0.1;;

    public DiffyDrivePod(HardwareMap hw, String top, String bottom, String encoder){
        this.top = hw.get(DcMotorEx.class, top);
        this.bottom = hw.get(DcMotorEx.class, bottom);
        this.encoder = new CotEncoder(hw, encoder);
        this.pid = new PIDController(0,0,0,0);
    }

    //Calculates the rotation the swerve pod needs to rotate to.
    public void rotatePodTo(double x, double y){
        DirectionalVector currentAngle = encoder.getPosition();
        DirectionalVector clockWise = new DirectionalVector(x, y);
        //Inverted vector of clockwise
        DirectionalVector counterClockWise = new DirectionalVector(-x, -y);
        //Given the two angles, we can rotate clockwise (-) or counter clockwise (+)
        //Pick the direction that requires the least rotation distance
        //We use vector math instead of angles to preserve continuity and avoid
        //Edge cases with 0 and 360.
        double distClockwise = currentAngle.findDistance(clockWise);
        double distCounter = currentAngle.findDistance(counterClockWise);

        //If clockwise is closer, go clockwise
        if (distClockwise < distCounter){
            targetAngle = clockWise;
        }else{
        //else, if counterclockwise is closer, go to the counter clockwise solution
            targetAngle = counterClockWise;
        }

    }

    //Powers the pod given some power value of [-1,1].
    //-1 being backwards and 1 being forwards.
    public void drivePod(double power){

        double dist = targetAngle.findDistance(encoder.getPosition());
        if (dist < DRIVE_THRESHOLD){
            top.setPower(power * invertState);
            bottom.setPower(-power * invertState);
        }
    }

    /**
     * This runs an internal PID system
     * This takes the known target angle and known angle of the pod
     * and powers the motors to rotate the pod into position.
     * This also automatically calculates
     */
    public void update(){
        //Note: we used encoder.findDistance(targetAngle) in the rotatePodTo function
        //However, since it is a distance function, it is inherently positive
        double power = pid.calculate(targetAngle.findDistance(encoder.getPosition()), 0);

        //Determine the invert state based on where the pod is facing.
        //Suppose that any +y vector positions means the invert state is +
        //Then, any -y vector positions means it should be -
        //Use the target position.
        //This is to prevent edge cases
        if(targetAngle.getY() < 0){
            invertState = -1;
        }
        else{
            invertState = 1;
        }

        //Update the pod's rotation as needed
        //But only do so if it meets the threshold to avoid oscillation (which should be solvable with
        //proper PID tuning. But for now, use a threshold
        if (power > POWER_THRESHOLD){
            //To determine the direction, we will base it on the x comparison
            //between the target and actual
            //If tarX > actX --> direction = -1 (clock wise)
            //If tarX < actX --> direction = 1 (counter clockwise)
            int direction = targetAngle.getX() > encoder.getPosition().getX() ? -1 : 1;
            top.setPower(power * direction);
            bottom.setPower(power * direction);
        }
    }

    /**
     * Drives the swerve pod.
     * This will be called from within the TeleOp loop
     * @param xStick The x-portion of the joystick input
     * @param yStick The y-portion of the joystick input
     */
    public void drive(double xStick, double yStick){
        //Changes the direction of the pod
        //Intended behavior is to "save" the last known direction
        //if the driver stops providing input (x = 0, y = 0)
        double magnitude = xStick * xStick + yStick * yStick;
        if (magnitude < POWER_THRESHOLD) {
            rotatePodTo(xStick, yStick);
        }
        //Power is just the magnitude of the joystick input
        drivePod(magnitude);

        //Updates the rotation of the pod
        update();
    }

}