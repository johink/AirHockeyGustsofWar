package onyeka.is.great.airhockeygustsofwar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.baseLayout);

        gameView = new GameView(this, layout);

        layout.addView(gameView);
    }
}
