package com.fmr.findmyroom.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fmr.findmyroom.R;
import com.fmr.findmyroom.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashImageView;
        Animation splashAnimation;

        splashImageView = findViewById(R.id.splashImage);
        splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_transition);

        // animate splash screen
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
