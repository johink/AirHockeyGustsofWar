package onyeka.is.great.airhockeygustsofwar;

import android.widget.ImageView;

import java.util.Random;

/**
 * Created by John on 4/9/2016.
 */
public class Puck extends GameObject {

    public Puck(ImageView iv, int x, int y, int w, int h, int maxSpd, int maxAcc, int xVelo, int yVelo, int xAcc, int yAcc, String name)
    {
        super(iv, x, y, w, h, maxSpd, maxAcc, xVelo, yVelo, xAcc, yAcc, name);
        mass = 5;
    }

    public Puck(ImageView iv, int x, int y, int w, int h, int maxSpd, int maxAcc, String name) {
        super(iv, x, y, w, h, maxSpd, maxAcc, name);
        mass = 5;
    }

    @Override
    public void update(double elapsed){
        //Update position
        xPos += Math.round(xVelocity);
        yPos += Math.round(yVelocity);

        //Check if position went off screen
        if (xPos < width * 5 / 2) {
            xVelocity = -xVelocity * GameView.DEFAULT_COLLISION_DAMPENING;
            xPos = width * 5 / 2;
        }
        if (xPos > GameView.screenWidth - width * 2) {
            xVelocity = -xVelocity * GameView.DEFAULT_COLLISION_DAMPENING;
            xPos = GameView.screenWidth - width * 2;
        }
        if (yPos < height * 6 / 5) {
            yVelocity = -yVelocity * GameView.DEFAULT_COLLISION_DAMPENING;
            yPos = height * 6 / 5;
        }
        if (yPos > GameView.screenHeight - height) {
            yVelocity = -yVelocity * GameView.DEFAULT_COLLISION_DAMPENING;
            yPos = GameView.screenHeight - height;
        }

        yVelocity *= GameView.DEFAULT_PUCK_FRICTION;
        xVelocity *= GameView.DEFAULT_PUCK_FRICTION;

    }

    @Override
    public void reset() {
        Random rand = new Random();
        xVelocity = (rand.nextFloat() - .5) * 5;
        yVelocity = (rand.nextFloat() - .5) * 5;
        xPos = GameView.screenWidth / 2;
        yPos = GameView.screenHeight / 2;
    }

    @Override
    public void draw() {
        //Set the image's coordinates to the previously-calculated positions
        image.setX(xPos - width / 2);
        image.setY(yPos - height / 2);
    }
}
