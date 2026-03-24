package org.firstinspires.ftc.teamcode.auto;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Autonomous
public class AutoReplay extends LinearOpMode {
    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {
        //Init Phase

        ElapsedTime timer = new ElapsedTime();
        double time;
        JSONHandler fileSystem;
        JSONArray movement, events;
        JSONObject currMove, currEvent, nextMove, nextEvent;
        int movement_index = 0, events_index = 0;
        try {
            fileSystem = new JSONHandler();
            fileSystem.loadReplay();
            movement = fileSystem.getMovement();
            events = fileSystem.getMovement();
            nextMove = movement.getJSONObject(movement_index);
            nextEvent = events.getJSONObject(events_index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        waitForStart();
        //Start Phase
        timer.reset();
        while(opModeIsActive()){
            time = timer.milliseconds();


            try {
                /*Movement data
                {"t": 0.00, "x": 0.0, "y": 0.0, "heading": 0.0}*/
                if(nextMove.getDouble("t") > time){
                    //Set the new target position based on these values
                    //nextMove.getDouble("x");
                    //nextMove.getDouble("y");
                    //nextMove.getDouble("heading");
                    movement_index++;
                    nextMove = movement.getJSONObject(movement_index);
                }
                telemetry.addData("Time Trigger",nextMove.getDouble("t"));
                telemetry.addLine(String.format("Current (x, y, h): (%.2f, %.2f, %.2f)\n",
                        nextMove.getDouble("x"),
                        nextMove.getDouble("y"),
                        nextMove.getDouble("heading")));

                /*Event Data
                {"t": 1.23, "event": "<SomeFunctionCall>"}*/
                if(nextEvent.getDouble("t") > time){
                    //Do something with the function information
                    switch(nextEvent.getString("event")){
                        case "Function1":
                            break;
                        case "Function2":
                            break;
                        //Add more cases as necessary
                        default:
                            break;
                    }
                    events_index++;
                    nextEvent = events.getJSONObject(events_index);
                }
                telemetry.addData("Time Trigger",nextEvent.getDouble("t"));
                telemetry.addData("Current Function: ", nextMove.getString("event"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            telemetry.addLine(String.format("Time: %.2f\n",timer.milliseconds()/1000.0));
            telemetry.update();
        }
    }

}
