package com.sk.socialmediaintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;

public class start extends AppCompatActivity {

    LottieAnimationView googleicon,facebookicon2,smicons;
    TextView social,media,integration;
    ImageView imgv;
    Animation animtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        animtext = AnimationUtils.loadAnimation(this,R.anim.text_animation);
        googleicon = findViewById(R.id.googleicon);
        smicons = findViewById(R.id.loding);
        facebookicon2 = findViewById(R.id.facebookicon2);
        social = findViewById(R.id.socialtv);
        media = findViewById(R.id.mediatv);
        integration = findViewById(R.id.integrationtv);
        imgv = findViewById(R.id.bgimg);

        // Animation

        social.setAnimation(animtext);
        media.setAnimation(animtext);
        integration.setAnimation(animtext);

        // Splash Screen

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(start.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);

    }

}