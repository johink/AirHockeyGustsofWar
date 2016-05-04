package onyeka.is.great.airhockeygustsofwar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by John on 5/1/2016.
 */
public class MainMenuActivity extends AppCompatActivity {

    private Button btnSingle, btnLocal, btnOnline, btnShare;
    private Context context;
    View.OnClickListener localListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mIntent = new Intent(context, MainActivity.class);
            startActivity(mIntent);
        }
    };

    View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", "8675309");
            smsIntent.putExtra("sms_body","Hey, check out this game!  \"Air Hockey:  Gusts of War\", available on the Google Play Store!");
            startActivity(smsIntent);
        }
    };

    View.OnLongClickListener playerListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);



            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        context = this;
        btnSingle = (Button) findViewById(R.id.btnSingle);
        btnLocal = (Button) findViewById(R.id.btnLocal);
        btnOnline = (Button) findViewById(R.id.btnOnline);
        btnShare = (Button) findViewById(R.id.btnShare);

        btnLocal.setOnClickListener(localListener);
        btnShare.setOnClickListener(shareListener);
    }

}
