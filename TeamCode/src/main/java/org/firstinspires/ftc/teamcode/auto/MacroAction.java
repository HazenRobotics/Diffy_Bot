package org.firstinspires.ftc.teamcode.auto;

/**
 * This class encapsulates a single autonomous action
 * This could be a drive action, a subsystem action etc.
 */
public abstract class MacroAction {

    //The provided start time and how long this action should take
    private double startTime, runTime;
    private MacroAction next;

    //Constructor
    public MacroAction(){
        this(0,0, null);
    }
    public MacroAction(double start, double run){
        this(start, run, null);
    }
    public MacroAction(double start, double run, MacroAction next){
        startTime = start;
        runTime = run;
        this.next = next;
    }

    /**
     * This is the primary function
     * Inside of run() will be an implementation
     * of some sort of action a subsystem will take
     * within it's allocated time
     */
    public abstract void run();

    /**
     * A simple function to trigger the next action.
     */
    public void runNext(){
        next.run();
    }

    /**
     * Scale the action's time by a multiplier
     * In particular, this is useful for slowing actions down
     * @param multiplier Value to change time scale
     */
    public void speedScale(double multiplier){
        startTime *= multiplier;
        runTime *= multiplier;
    }



    //Standard getters
    public double getStartTime() {return startTime;}
    public double getRunTime(){return runTime;}
    public MacroAction getNext(){return next;}

    //Standard setters
    public void setStartTime(double time){startTime=time;}
    public void getRunTime(double time){runTime=time;}
    public void setNext(MacroAction action){next=action;}


}
