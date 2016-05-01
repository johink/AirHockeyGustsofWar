package onyeka.is.great.airhockeygustsofwar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TitleActivity extends AppCompatActivity {

    private ImageView iv_logo;
    private Context context;
    private RelativeLayout titleLayout;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mIntent = new Intent(context, MainMenuActivity.class);
            // mIntent.setAction("android.intent.ACTION.VIEW");
            startActivity(mIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        context = this;
        iv_logo = new ImageView(this);

        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        titleLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        titleLayout.addView(iv_logo);

        iv_logo.setImageDrawable(getResources().getDrawable(R.drawable.gow_logo));
        iv_logo.setOnClickListener(listener);
    }
}
