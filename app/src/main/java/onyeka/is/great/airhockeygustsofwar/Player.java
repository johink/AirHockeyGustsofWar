package onyeka.is.great.airhockeygustsofwar;

import android.widget.ImageView;

import java.net.Socket;

/**
 * Created by John on 4/7/2016.
 */
public class Player {

    public String PlayerName;
    public int Points;
    public int PlayerNumber;
    public Paddle paddle;
    private Socket playerSocket;

    public Player(String name, int number, Socket s, Paddle p)
    {
        PlayerName = name;
        PlayerNumber = number;
        playerSocket = s;
        paddle = p;
        p.section = number - 1;
    }

    public Player(String name, int number, Paddle p)
    {
        PlayerName = name;
        PlayerNumber = number;
        playerSocket = null;
        paddle = p;
        p.section = number - 1;
    }
}
