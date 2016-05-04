package onyeka.is.great.airhockeygustsofwar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

public class TitleActivity extends AppCompatActivity {

    Context context;
    ViewGroup titleLayoutGroup;
    LinearLayout mainMenuLayout;
    Button btnSingle, btnLocal, btnOnline, btnShare;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        context = this;

        final ImageView logo = (ImageView) findViewById(R.id.logoView);
        titleLayoutGroup = (ViewGroup) findViewById(R.id.titleLayout);
        mainMenuLayout = (LinearLayout) findViewById(R.id.mainMenuLayout);

        // MainMenuActivity.onCreate()
        btnSingle = (Button) findViewById(R.id.btnSingle);
        btnLocal = (Button) findViewById(R.id.btnLocal);
        btnOnline = (Button) findViewById(R.id.btnOnline);
        btnShare = (Button) findViewById(R.id.btnShare);
        // end MainMenuActivity.onCreate()

        final Animation fadeinAnimation =
                AnimationUtils.loadAnimation(this, R.anim.logo_fadein_anim); // title logo fades in
        final Animation menu_fadeInAnimation =
                AnimationUtils.loadAnimation(this, R.anim.mainmenu_fadein_anim); // main menu fades in

        fadeinAnimation.setAnimationListener(new Animation.AnimationListener() {
             @Override
             public void onAnimationStart(Animation animation) {

             }

             @Override
             public void onAnimationEnd(Animation animation) {
                 ObjectAnimator transAnimation = ObjectAnimator.ofFloat(logo, "translationY", -450);
                 transAnimation.setInterpolator(new LinearInterpolator());
                 transAnimation.setDuration(2000);
                 transAnimation.start();
             }

             @Override
             public void onAnimationRepeat(Animation animation) {

             }
         });

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
                RelativeLayout rl = new RelativeLayout(context);
                Button dialog_button_next, dialog_button_cancel;
                final EditText editText = new EditText(context);
                builder.setTitle(getString(R.string.dialog_title));

                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.weight=1;

                builder.setPositiveButton(getString(R.string.dialog_button_next), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPref = TitleActivity.this.getSharedPreferences("NAME", context.MODE_PRIVATE);
                        String name = editText.getText().toString();
                        editor.putString(getString(R.string.sharedprefs_playername_key), name);
                        // saves name to SharedPreferences
                    }
                });

                builder.setNegativeButton(getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                rl.addView(editText, textParams);
                builder.setView(rl);

                builder.show();
                return true;
            }
        };

        btnLocal.setOnClickListener(localListener);
        btnLocal.setOnLongClickListener(playerListener);
        btnShare.setOnClickListener(shareListener);

        logo.startAnimation(fadeinAnimation);
        mainMenuLayout.startAnimation(menu_fadeInAnimation);

/*        View.OnClickListener logoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenu();
            }
        };
        logo.setOnClickListener(logoListener);*/ // dont need to click on logo anymore
    }

    // deprecated
    public void mainMenu()
    {
        Intent mIntent = new Intent(context, MainMenuActivity.class);
        // mIntent.setAction("android.intent.ACTION.VIEW");
        startActivity(mIntent);
    }
}
