package onyeka.is.great.airhockeygustsofwar;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import java.util.List;

public class TitleActivity extends AppCompatActivity {

    Context context;
    ViewGroup titleLayoutGroup;
    LinearLayout mainMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        context = this;

        final ImageView logo = (ImageView) findViewById(R.id.logoView);
        titleLayoutGroup = (ViewGroup) findViewById(R.id.titleLayout);
        mainMenuLayout = (LinearLayout) findViewById(R.id.mainMenuLayout);
        AnimationSet animationSet = new AnimationSet(true);
        // AnimatorSet animatorSet = new AnimatorSet();
        // AnimatorInflater animatorInflater = new AnimatorInflater();



        final Animation fadeinAnimation =
                AnimationUtils.loadAnimation(this, R.anim.logo_fadein_anim); // title logo fades in
        final Animation scrollUpAnimation =
                AnimationUtils.loadAnimation(this, R.anim.logo_scrollup_anim); // title logo scrolls up
        final Animation menu_fadeInAnimation =
                AnimationUtils.loadAnimation(this, R.anim.mainmenu_fadein_anim); // main menu fades in

/*      animationSet.addAnimation(fadeinAnimation);
        animationSet.addAnimation(scrollUpAnimation);
        animationSet.addAnimation(menu_fadeInAnimation);*/

        fadeinAnimation.setAnimationListener(new Animation.AnimationListener() {
             @Override
             public void onAnimationStart(Animation animation) {

             }

             @Override
             public void onAnimationEnd(Animation animation) {
                 logo.clearAnimation();
                scrollUpAnimation.start();
             }

             @Override
             public void onAnimationRepeat(Animation animation) {

             }
         });

        scrollUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mainMenuLayout.setAlpha(1);
                scrollUpAnimation.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.clearAnimation();
                menu_fadeInAnimation.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        menu_fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                menu_fadeInAnimation.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logo.startAnimation(fadeinAnimation);


        // animatorSet.playSequentially(fadeinAnimation);

        // logo.startAnimation(scrollUpAnimation);
        //mainMenuLayout.startAnimation(menu_fadeInAnimation);



        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLogo();
                mainMenu();
            }
        };

        logo.setOnClickListener(listener);
        //logo.setOnClickListener(listener);
    }

    public void moveLogo()
    {

    }

    public void mainMenu()
    {
        Intent mIntent = new Intent(context, MainMenuActivity.class);
        // mIntent.setAction("android.intent.ACTION.VIEW");
        startActivity(mIntent);
    }
}
