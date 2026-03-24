package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.GamepadEvents;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

@TeleOp(name="Auto Recorder", group = "Auto")
public class AutoRecorder extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Init Phase
        //Initialize Recording System
        JSONHandler fileSystem = new JSONHandler();
        boolean recording = false;
        ElapsedTime timer = new ElapsedTime();
        double time;

        //Initialize Controls
        GamepadEvents controller1 = new GamepadEvents(gamepad1);
        GamepadEvents controller2 = new GamepadEvents(gamepad2);

        //Initialize Robot

        waitForStart();
        //Start Phase
        timer.reset();
        while(opModeIsActive()){
            //This is so that for this entire loop, everything is on the same "cycle"
            time = timer.milliseconds();
            //Toggle the recording button
            if(controller2.a.onPress()){
                recording = !recording;
            }
            //Save the current recording
            if(controller2.b.onPress()){
                recording = false;
                break;
            }
            //Wipe the current recording (Recording must be false)
            if(controller2.x.onPress() && !recording){
                fileSystem.resetRecording();
            }


            //For recording movement
            if(recording){
                try {
                    fileSystem.recordMovement(time,
                            0/*X-Localization*/,
                            0/*Y-Localization*/,
                            0/*Orientation*/);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            telemetry.addData("Recording: ",recording);
            telemetry.update();
        }

        //Saving the recorded data
        telemetry.addLine("Saving file...");
        telemetry.update();
        try {
            fileSystem.saveReplay();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Completion message
        timer.reset();
        while(timer.seconds() < 10) {
            telemetry.addLine("Successfully saved file");
            telemetry.addData("Ending OpMode in: ", 10-timer.seconds());
            telemetry.update();
            sleep(500);
        }
    }




}
