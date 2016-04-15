package onyeka.is.great.airhockeygustsofwar;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.Console;

/**
 * Created by John on 4/7/2016.
 */
public class Paddle extends GameObject {

    Player player;
    public int section;
    int resetX, resetY;
    double xWeight, yWeight, accelWeight;
    int inertiaFactor = 3;
    public boolean held, reading;
    View.OnTouchListener paddleListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);

            switch (action) {
                case (MotionEvent.ACTION_DOWN):
                    held = true;
                    Log.d(GameView.DEBUG_TAG, "Action was DOWN");
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    if(held && reading) {
                        targetX = (int) event.getRawX();
                        targetY = (int) event.getRawY() - height;

                        if(targetX < 2 * width)
                            targetX = 2 * width;
                        if(targetX > GameView.screenWidth - width * 3 / 2)
                            targetX = GameView.screenWidth - width * 3 / 2;
                        if(targetY < height * 6 / 5 + (GameView.screenHeight / 2 - height / 2) * section)
                            targetY = height * 6 / 5 + (GameView.screenHeight / 2 - height / 2) * section;
                        if(targetY > GameView.screenHeight / 2 + (GameView.screenHeight / 2 - height * 3 / 4) * section)
                            targetY = GameView.screenHeight / 2 + (GameView.screenHeight / 2 - height * 3 / 4) * section;

                        reading = false;
                    }
                    return true;
                case (MotionEvent.ACTION_UP):
                    xAccel = yAccel = accelWeight = yWeight = xWeight = 0;
                    held = false;
                    Log.d(GameView.DEBUG_TAG, "Action was UP");
                    return true;
                default:
                    return true;
            }
        }
    };

    public Paddle(ImageView iv, int x, int y, int w, int h, int maxSpd, int maxAcc, int xVelo, int yVelo, int xAcc, int yAcc, String name)
    {
        super(iv, x, y, w, h, maxSpd, maxAcc, xVelo, yVelo, xAcc, yAcc, name);
        targetX = xPos;
        resetX = xPos;
        targetY = yPos;
        resetY = yPos;
        mass = 10;

        image.setOnTouchListener(paddleListener);
    }

    public Paddle(ImageView iv, int x, int y, int w, int h, int maxSpd, int maxAcc, String name) {
        super(iv, x, y, w, h, maxSpd, maxAcc, name);
        targetX = xPos;
        targetY = yPos;
        resetX = xPos;
        resetY = yPos;
        mass = 10;

        image.setOnTouchListener(paddleListener);
    }

    @Override
    public void draw() {
        //Set the image's coordinates to the previously-calculated positions
        image.setX(xPos - width / 2);
        image.setY(yPos - height / 2);
    }
    @Override
    public int getMass()
    {
        return mass * (held ? 3 : 1);
    }

    @Override
    public void update(double interval) {

        //Only update acceleration if the player is holding the paddle
        if(held) {
            int deltaX = targetX - xPos;
            int deltaY = targetY - yPos;

            //If we're at the target, stop and do nothing
            if (deltaX == 0 && deltaY == 0) {
                xVelocity = yVelocity = xAccel = yAccel = xWeight = yWeight = accelWeight = 0;
            } else {
                double deltaXSquared = deltaX * deltaX;
                double deltaYSquared = deltaY * deltaY;

                accelWeight = (Math.abs(deltaX) + Math.abs(deltaY)) / (width + height * 1.f) / 2;

                if (accelWeight > 1)
                    accelWeight = 1.0;
                else if (accelWeight < .2)
                    accelWeight = .2;

                xWeight = Math.sqrt(deltaXSquared / (deltaXSquared + deltaYSquared));
                yWeight = Math.sqrt(deltaYSquared / (deltaXSquared + deltaYSquared));

                if (deltaX < 0)
                    xWeight = -xWeight;
                if (deltaY < 0)
                    yWeight = -yWeight;

                double intervalAccel = maxAccel / 3;

                xAccel += inertiaFactor * intervalAccel * accelWeight * xWeight;
                yAccel += inertiaFactor * intervalAccel * accelWeight * yWeight;
                xAccel /= inertiaFactor + 1;
                yAccel /= inertiaFactor + 1;
                //Normalize acceleration vectors
                if (xAccel * xAccel + yAccel * yAccel > maxAccel * maxAccel) {
                    double totalAccelSq = xAccel * xAccel + yAccel * yAccel;
                    xAccel = xAccel > 0 ? Math.sqrt(xAccel * xAccel * maxAccel * maxAccel / totalAccelSq) : -Math.sqrt(xAccel * xAccel * maxAccel * maxAccel / totalAccelSq);
                    yAccel = yAccel > 0 ? Math.sqrt(yAccel * yAccel * maxAccel * maxAccel / totalAccelSq) : -Math.sqrt(yAccel * yAccel * maxAccel * maxAccel / totalAccelSq);
                }

            }

            xVelocity += xAccel;
            yVelocity += yAccel;
            //Normalize velocity vectors
            if (xVelocity * xVelocity + yVelocity * yVelocity > topSpeed * topSpeed) {
                double totalSpeedSq = xVelocity * xVelocity + yVelocity * yVelocity;
                xVelocity = xVelocity > 0 ? Math.sqrt(xVelocity * xVelocity * topSpeed * topSpeed / totalSpeedSq) : -Math.sqrt(xVelocity * xVelocity * topSpeed * topSpeed / totalSpeedSq);
                yVelocity = yVelocity > 0 ? Math.sqrt(yVelocity * yVelocity * topSpeed * topSpeed / totalSpeedSq) : -Math.sqrt(yVelocity * yVelocity * topSpeed * topSpeed / totalSpeedSq);
            }

            //Check if velocity can get us to the target
            if (xPos < targetX && xPos + xVelocity >= targetX || xPos > targetX && xPos + xVelocity <= targetX) {
                xPos = targetX;
                xVelocity = xAccel = 0;
            }
            if (yPos < targetY && yPos + yVelocity >= targetY || yPos > targetY && yPos + yVelocity <= targetY) {
                yPos = targetY;
                yVelocity = yAccel = 0;
            }



            //Log.d(GameView.DEBUG_TAG, "xPos: " + xPos + ", yPos: " + yPos + ", xVel: " + xVelocity + ", yVel: " + yVelocity + ", xAcc: " + xAccel + ", yAcc: " + yAccel);

        }

        //Update position
        xPos += Math.round(xVelocity);
        yPos += Math.round(yVelocity);

        //Check if position went off screen
        if (xPos < 2 * width) {
            xAccel = xVelocity = 0;
            xPos = 2 * width;
        }
        if (xPos > GameView.screenWidth - width * 3 / 2) {
            xAccel = xVelocity = 0;
            xPos = GameView.screenWidth - width * 3 / 2;
        }
        if (yPos < height * 6 / 5 + (GameView.screenHeight / 2 - height / 2) * section) {
            yAccel = yVelocity = 0;
            yPos = height * 6 / 5 + (GameView.screenHeight / 2 - height / 2) * section;
        }
        if (yPos > GameView.screenHeight / 2 + (GameView.screenHeight / 2 - height * 3 / 4) * section) {
            yAccel = yVelocity = 0;
            yPos = GameView.screenHeight / 2 + (GameView.screenHeight / 2 - height * 3 / 4) * section;
        }

        //Apply friction to the paddle
        xVelocity *= GameView.DEFAULT_FRICTION;
        yVelocity *= GameView.DEFAULT_FRICTION;

        //Log.d(GameView.DEBUG_TAG, name + "'s angle: " + getAngle());
        //Log.d(GameView.DEBUG_TAG, "xVel: " + xVelocity + ", yVel: " + yVelocity + ", xAcc: " + xAccel + ", yAcc: " + yAccel);
    }

    @Override
    public void reset() {
        xAccel = yAccel = xVelocity = yVelocity = 0;
        xPos = resetX;
        yPos = resetY;
    }
}
