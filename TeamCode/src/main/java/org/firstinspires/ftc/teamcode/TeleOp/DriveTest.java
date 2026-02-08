package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.DiffyDrive;
import org.firstinspires.ftc.teamcode.utils.GamepadEvents;

public class DriveTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Init Phase
        DiffyDrive drive = new DiffyDrive(hardwareMap);
        GamepadEvents controller1 = new GamepadEvents(gamepad1);
        waitForStart();
        //Start Phase
        double forward, strafe, rotate;
        while(opModeIsActive()){
            forward = controller1.left_stick_y;
            strafe = controller1.left_stick_x;
            rotate = controller1.right_stick_x;

            drive.drive(forward,strafe,rotate);

            telemetry.addData("Forward: ",forward);
            telemetry.addData("Strafe: ",strafe);
            telemetry.addData("Rotate: ",rotate);
            telemetry.addLine(drive.toString());
            telemetry.update();
        }
    }
}
