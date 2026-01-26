package org.firstinspires.ftc.teamcode.utils;

public class DirectionalVector {

    private double x,y;

    //Initializes a 0 magnitude vector
    public DirectionalVector(){
        this(0,0);
    }

    /**Initializes vector using an angle in degrees
     * @param degrees Direction angle in degrees
     */
    public DirectionalVector(double degrees){
        setAngleDegrees(degrees);
    }

    /**Initializes a vector with specified direction vector components
     *
     * @param x X component of the vector
     * @param y Y component of the vector
     */
    public DirectionalVector(double x, double y){
        this.x = x;
        this.y = y;
    }


    //Basic getters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    //Basic setters
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    //Returns the angle of this vector in degrees
    public double getAngle(){
        return Math.toDegrees(Math.atan2(x, y));
    }

    /**Modifies the x-y components to point in the direction of the angle (Degrees)
     *
     */
    public void setAngleDegrees(double degrees){
        setAngleRadians(Math.toRadians(degrees));
    }

    /**Modifies the x-y components to point in the direction of the angle (Radians)
     */
     public void setAngleRadians(double radians){
        this.x = Math.cos(radians);
        this.y = Math.sin(radians);
    }

    //Basic arithmetic functions

    //Adds the other vector to this vector.
    public void addVector(DirectionalVector other){
        this.x += other.getX();
        this.y += other.getY();
    }

    //Scales the x and y values by the scale factor
    public void scaleVector(double scale){
        this.x *= scale;
        this.y *= scale;
    }

    /**
     * @return Returns the calculated distance between this vector point and another vector
     */
    public double findDistance(DirectionalVector other){
         return Math.sqrt(
                 Math.pow(x - other.getX(),2) +
                 Math.pow(y - other.getY(),2)
         );
    }

    //Scales the x and y values such that they have a collective magnitude of 1
    public void normalize(){
        double curMagnitude = Math.sqrt(x * x + y * y);
        //Can't scale by 0
        if (curMagnitude == 0){
            this.x = 0;
            this.y = 0;
        }
        //Prevents dividing by 0 if the magnitude is 0.
        else {
            this.x /= curMagnitude;
            this.y /= curMagnitude;
        }
    }
}
