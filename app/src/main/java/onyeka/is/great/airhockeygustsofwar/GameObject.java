package onyeka.is.great.airhockeygustsofwar;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

/**
 * Created by John on 4/6/2016.
 */
public class GameObject {

    public String name;
    protected ImageView image;
    protected int xPos, yPos, width, height;
    protected double xVelocity, yVelocity, xAccel, yAccel;
    protected double topSpeed, maxAccel;

    public int mass;

    public GameObject(ImageView iv, int x, int y, int w, int h, int maxSpd, int maxAcc, int xVelo, int yVelo, int xAcc, int yAcc, String n)
    {
        image = iv;
        xPos = x;
        yPos = y;
        width = w;
        height = h;
        topSpeed = maxSpd;
        maxAccel = maxAcc;
        xVelocity = xVelo;
        yVelocity = yVelo;
        xAccel = xAcc;
        yAccel = yAcc;
        name = n;
    }

    public GameObject(ImageView iv, int x, int y, int w, int h, int maxSpd, int maxAcc, String n)
    {
        image = iv;
        xPos = x;
        yPos = y;
        width = w;
        height = h;
        topSpeed = maxSpd;
        maxAccel = maxAcc;
        xVelocity = 0;
        yVelocity = 0;
        xAccel = 0;
        yAccel = 0;
        name = n;
    }

    public void update(double interval)
    {
        //TODO add update code
    }

    public void draw()
    {
        //TODO add draw code
    }

    @Override
    public String toString() {
        return name;
    }

    public double getAngle()
    {
        return Math.atan2(yVelocity, xVelocity);
    }

    public static double getAngle(double xVelo, double yVelo)
    {
        return Math.atan2(yVelo, xVelo);
    }

    public double getSpeed()
    {
        return Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);
    }

    public int getMass()
    {
        return mass;
    }
}
