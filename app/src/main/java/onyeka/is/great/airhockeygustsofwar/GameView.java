package onyeka.is.great.airhockeygustsofwar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 4/6/2016.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final String DEBUG_TAG = "Gusts of War";
    public static final int PADDLE_DEFAULT_MAX_SPEED = 30;
    public static final int PADDLE_DEFAULT_MAX_ACCEL = 6;
    public static final int PUCK_DEFAULT_MAX_SPEED = 50;
    public static final int PUCK_DEFAULT_MAX_ACCEL = 20;
    public static final int MAX_FPS = 30;
    public static final float DEFAULT_FRICTION = .98f;
    public static final float DEFAULT_PUCK_FRICTION = .995f;
    public static final float DEFAULT_COLLISION_DAMPENING = .9f;
    public static final int PUCK_SIZE = 100;
    public static final int PADDLE_SIZE = 150;

    private RelativeLayout parentLayout;
    private GameThread thread;
    private List<GameObject> gameObjectList;
    private List<Player> players;
    private SoundPool soundPool;
    public static int screenWidth, screenHeight;
    private LayoutInflater layoutInflater;
    private Context _context;
    private boolean collision = false, setup = false;
    private Puck thePuck;
    private TextView p1Score, p2Score;
    private SharedPreferences sharedPref;

    public GameView(Context context, RelativeLayout parent)
    {
        super(context);

        getHolder().addCallback(this);

        p1Score = (TextView) parent.findViewById(R.id.txtP1Score);
        p2Score = (TextView) parent.findViewById(R.id.txtP2Score);

        parentLayout = parent;
        _context = context;
        players = new ArrayList<>();
        gameObjectList = new ArrayList<>();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        sharedPref = context.getSharedPreferences(TitleActivity.GAME_NAME,Context.MODE_PRIVATE);

        //TODO build the soundpool
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        screenWidth = w;
        screenHeight = h;

        newGame();
        setup = true;
    }

    private void newGame() {
        if(setup)
            return;
        ImageView background = (ImageView) layoutInflater.inflate(R.layout.game_object, null);
        background.setImageResource(R.drawable.background);
        background.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, screenHeight));

        ImageView p1Paddle = (ImageView) layoutInflater.inflate(R.layout.game_object, null);
        p1Paddle.setImageResource(R.drawable.paddle);
        p1Paddle.setLayoutParams(new RelativeLayout.LayoutParams(PADDLE_SIZE, PADDLE_SIZE));
        p1Paddle.setX(screenWidth / 2 - PADDLE_SIZE / 2);
        p1Paddle.setY(PADDLE_SIZE / 2);

        ImageView p2Paddle = (ImageView) layoutInflater.inflate(R.layout.game_object, null);
        p2Paddle.setImageResource(R.drawable.paddle);
        p2Paddle.setLayoutParams(new RelativeLayout.LayoutParams(PADDLE_SIZE, PADDLE_SIZE));
        p2Paddle.setX(screenWidth / 2 - PADDLE_SIZE / 2);
        p2Paddle.setY(screenHeight - (int)(PADDLE_SIZE * 1.5));

        ImageView puckPic = (ImageView) layoutInflater.inflate(R.layout.game_object, null);
        puckPic.setImageResource(R.drawable.puck);
        puckPic.setLayoutParams(new RelativeLayout.LayoutParams(PUCK_SIZE, PUCK_SIZE));
        puckPic.setX(screenWidth / 2 - PUCK_SIZE / 2);
        puckPic.setY(screenHeight / 2 - PUCK_SIZE / 2);

        parentLayout.addView(background);
        parentLayout.addView(puckPic);
        parentLayout.addView(p1Paddle);
        parentLayout.addView(p2Paddle);

        // constants?
        Paddle p1 = new Paddle(p1Paddle,screenWidth/2 - PADDLE_SIZE / 2, PADDLE_SIZE / 2, PADDLE_SIZE, PADDLE_SIZE, PADDLE_DEFAULT_MAX_SPEED, PADDLE_DEFAULT_MAX_ACCEL, "P1 Paddle");
        Paddle p2 = new Paddle(p2Paddle,screenWidth/2 - PADDLE_SIZE / 2, screenHeight - (int)(PADDLE_SIZE * 1.5), PADDLE_SIZE, PADDLE_SIZE, PADDLE_DEFAULT_MAX_SPEED, PADDLE_DEFAULT_MAX_ACCEL, "P2 Paddle");
        Puck puck = new Puck(puckPic, screenWidth/2 - PUCK_SIZE / 2, screenHeight/2 - PUCK_SIZE / 2 ,PUCK_SIZE, PUCK_SIZE, PUCK_DEFAULT_MAX_SPEED, PUCK_DEFAULT_MAX_ACCEL, "Puck");

        gameObjectList.add(p1);
        gameObjectList.add(p2);
        gameObjectList.add(puck);

        thePuck = puck;

        players.add(new Player(sharedPref.getString(TitleActivity.P1_NAME,"Player One"), 1, p1));
        players.add(new Player(sharedPref.getString(TitleActivity.P2_NAME, "Player Two"), 2, p2));

        updateCharts();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Canvas c = holder.lockCanvas();

        try {
            synchronized (holder) {
                c.drawRect(0, 0, screenWidth, screenHeight, new Paint(Color.WHITE));
            }
        }
        finally{
            if(c != null)
                holder.unlockCanvasAndPost(c);
        }

        thread = new GameThread(holder);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ensure that thread terminates properly
        boolean retry = true;
        thread.setRunning(false);

        while (retry)
        {
            try
            {
                thread.join(); //block current thread until cannonThread terminates
                retry = false;
            } // end try
            catch (InterruptedException e)
            {
            } // end catch
        } // end while
    }

    public void updatePositions(double interval)
    {
        for(GameObject go : gameObjectList)
        {
            go.update(interval);
        }

        for (int i = 0; i < gameObjectList.size(); i++) {
            for (int j = i + 1; j < gameObjectList.size(); j++) {
                GameObject ob1 = gameObjectList.get(i);
                GameObject ob2 = gameObjectList.get(j);

                double distance = Math.sqrt(Math.pow(ob1.xPos - ob2.xPos, 2) + Math.pow(ob1.yPos - ob2.yPos, 2));
                if (distance < ob1.width / 2 + ob2.width / 2) {
                        collide(ob1, ob2);}
            }
        }

    }

    private void collide(GameObject ob1, GameObject ob2) {
        ob1.xAccel = ob1.yAccel = ob2.xAccel = ob1.yAccel = 0;

        double angleBetweenObjects = GameObject.getAngle(ob1.xPos  - ob2.xPos, ob1.yPos - ob2.yPos);

        double ob1FirstPart = (ob1.getSpeed() * Math.cos(ob1.getAngle() - angleBetweenObjects) *
                (ob1.getMass() - ob2.getMass()) + 2 * ob2.getMass() * ob2.getSpeed() * Math.cos(ob2.getAngle() - angleBetweenObjects)) / (ob1.getMass() + ob2.getMass());

        ob1.xVelocity = ob1FirstPart * Math.cos(angleBetweenObjects) + ob1.getSpeed() * Math.sin(ob1.getAngle() - angleBetweenObjects) * Math.cos(angleBetweenObjects + Math.PI / 2);
        ob1.yVelocity = ob1FirstPart * Math.sin(angleBetweenObjects) + ob1.getSpeed() * Math.sin(ob1.getAngle() - angleBetweenObjects) * Math.sin(angleBetweenObjects + Math.PI / 2);

        double ob2FirstPart = (ob2.getSpeed() * Math.cos(ob2.getAngle() - angleBetweenObjects) *
                (ob2.getMass() - ob1.getMass()) + 2 * ob1.getMass() * ob1.getSpeed() * Math.cos(ob1.getAngle() - angleBetweenObjects)) / (ob2.getMass() + ob1.getMass());

        ob2.xVelocity = ob2FirstPart * Math.cos(angleBetweenObjects) + ob2.getSpeed() * Math.sin(ob2.getAngle() - angleBetweenObjects) * Math.cos(angleBetweenObjects + Math.PI / 2);
        ob2.yVelocity = ob2FirstPart * Math.sin(angleBetweenObjects) + ob2.getSpeed() * Math.sin(ob2.getAngle() - angleBetweenObjects) * Math.sin(angleBetweenObjects + Math.PI / 2);

        double deltaX = ob1.xPos - ob2.xPos;
        double deltaY = ob1.yPos - ob2.yPos;
        double deltaLength = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        double scaleFactor = (ob1.width / 2 + ob2.width / 2 - deltaLength) / deltaLength;
        deltaX *= scaleFactor;
        deltaY *= scaleFactor;

        double inverseMass1 = 1.f / ob1.getMass();
        double inverseMass2 = 1.f / ob2.getMass();

        //Push them away so they don't get stuck
        ob1.xPos += (int) (deltaX * (inverseMass1 / (inverseMass1 + inverseMass2)));
        ob1.yPos += (int) (deltaY * (inverseMass1 / (inverseMass1 + inverseMass2)));
        ob2.xPos -= (int) (deltaX * (inverseMass2 / (inverseMass1 + inverseMass2)));
        ob2.yPos -= (int) (deltaY * (inverseMass2 / (inverseMass1 + inverseMass2)));
    }

    public void drawGameObjects()
    {
        ((Activity) _context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(GameObject go : gameObjectList)
                {
                    go.draw();
                }
            }
        });

    }


    private class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean _running = true;

        public GameThread(SurfaceHolder holder)
        {
            surfaceHolder = holder;
        }

        public void setRunning(boolean running)
        {
            _running = running;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            long previousTime = System.currentTimeMillis();

            while(_running)
            {
                long currentTime = System.currentTimeMillis();
                long elapsedTimeMS = currentTime - previousTime;
                for(GameObject go : gameObjectList)
                    if(go.getClass() == Paddle.class)
                        ((Paddle) go).reading = true;
                updatePositions(elapsedTimeMS);
                drawGameObjects();
                previousTime = currentTime;
                if(checkForScore())
                {
                    for(GameObject go : gameObjectList)
                    {
                        go.reset();
                    }
                }

                if(elapsedTimeMS < 1000 / MAX_FPS)
                {
                    try {
                        Thread.sleep((1000 / MAX_FPS) - elapsedTimeMS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
/*
                try{
                    canvas = surfaceHolder.lockCanvas();

                    synchronized (surfaceHolder)
                    {
                        long currentTime = System.currentTimeMillis();
                        double elapsedTimeMS = currentTime - previousTime;
                        updatePositions(elapsedTimeMS);
                        drawGameObjects(canvas);
                        previousTime = currentTime;
                    }
                }
                finally {
                    if(canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
*/
            }
        }
    }

    private void updateCharts(){
        ((Activity) _context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                p2Score.bringToFront();
                p1Score.bringToFront();
                p1Score.setText(getResources().getString(R.string.scoreboard, players.get(0).PlayerName, players.get(0).Points));
                p2Score.setText(getResources().getString(R.string.scoreboard,players.get(1).PlayerName,players.get(1).Points));
            }
        });

    }

    private boolean checkForScore() {
        if(thePuck.xPos >= screenWidth / 2 - 60 && thePuck.xPos <= screenWidth / 2 + 140)
        {
            if(thePuck.yPos <= 150)
            {
                Log.d(DEBUG_TAG, "P2 scores!");
                players.get(1).Points++;
                updateCharts();
                return true;
            }
            else if(thePuck.yPos >= screenHeight - 100)
            {
                Log.d(DEBUG_TAG, "P1 scores!");
                players.get(0).Points++;
                updateCharts();
                return true;
            }
        }
        return false;
    }
}
