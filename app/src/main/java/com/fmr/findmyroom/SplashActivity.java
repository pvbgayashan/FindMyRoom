package com.fmr.findmyroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashImageView;
        TextView splashTextView;
        Animation splashAnimation;

        splashImageView = findViewById(R.id.splashImage);
        splashTextView = findViewById(R.id.splashTxt);
        splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_transition);

        // animate splash screen
        splashTextView.startAnimation(splashAnimation);
        splashImageView.startAnimation(splashAnimation);

        // navigate to home screen
        final Intent homeScreenIntent = new Intent(this, MainActivity.class);

        Thread splashThread = new Thread() {

            public void run() {
                try {
                    sleep(3000);
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    startActivity(homeScreenIntent);
                    finish();
                }
            }
        };

        splashThread.start();
    }
}
