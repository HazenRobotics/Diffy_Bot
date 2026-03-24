package org.firstinspires.ftc.teamcode.auto;

import android.annotation.SuppressLint;
import android.os.Environment;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/*
Sample structure:
{
  "movement": [
    {"t": 0.00, "x": 0.0, "y": 0.0, "heading": 0.0},
    {"t": 0.05, "x": 0.3, "y": 0.0, "heading": 0.0}
  ],
  "events": [
    {"t": 1.23, "event": "intake_on"},
    {"t": 2.87, "event": "lift_to_level_2"}
  ]
}
 */
public class JSONHandler {
    @SuppressLint("SdCardPath")
    public static final String PATH = "/sdcard/FIRST/settings/";
    public final String FILE;
    public final int FILE_INDEX;

    private  JSONArray movementArray, eventArray;

    //Default constructor that will auto-populate a file for us
    public JSONHandler(){
        this("replay_auto", 0);
    }

    /**
     * Creates a new file to save the replay to
     * @param name Any file name
     * @param index Index to mark new files
     */
    @SuppressLint("DefaultLocale")
    public JSONHandler(String name, int index){
        FILE = String.format("%s%s%d.json" , PATH , name , index);
        FILE_INDEX = index;
        resetRecording();
    }


    //Recording Functions

    /**
     * Writes to a JSON file to store recorded data
     */
    public void saveReplay() throws JSONException {
        File file = new File(FILE);
        JSONObject replayData = new JSONObject();
        replayData.put("movement", movementArray);
        replayData.put("events", eventArray);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(replayData.toString(2)); // pretty-print with indentation
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets/clears the recording
     */
    public void resetRecording(){
        movementArray = new JSONArray();
        eventArray = new JSONArray();
    }

    /**
     *
     * @param t Time parameter
     * @param x X location (Typically Forward/Backward)
     * @param y Y location (Typically Left/Right)
     * @param heading Orientation
     * @throws JSONException
     */
    public void recordMovement(double t, double x, double y, double heading) throws JSONException {
        JSONObject sample = new JSONObject();
        sample.put("t", t);
        sample.put("x", x);
        sample.put("y", y);
        sample.put("heading", heading);

        movementArray.put(sample);
    }

    /**
     *
     * @param t Time Parameter
     * @param eventName Event Action
     * @throws JSONException
     */
    public void recordEvent(double t, String eventName) throws JSONException {
        JSONObject event = new JSONObject();
        event.put("t", t);
        event.put("event", eventName);

        eventArray.put(event);
    }

    //Replay Functions

    /**
     * Loads the replay file into the arrays
     * @throws Exception
     */
    public void loadReplay() throws Exception {
        String jsonText = readFile(new File(FILE));
        JSONObject replay = new JSONObject(jsonText);
        movementArray = replay.getJSONArray("movement");
        eventArray = replay.getJSONArray("events");
    }

    /**
     * @param file File to read from
     * @return a String representation of the file
     * @throws Exception
     */
    @NonNull
    private String readFile(File file) throws Exception {
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        scanner.close();
        return builder.toString();
    }

    public JSONArray getMovement(){
        return movementArray;
    }
    public JSONArray getEvents(){
        return eventArray;
    }


}
