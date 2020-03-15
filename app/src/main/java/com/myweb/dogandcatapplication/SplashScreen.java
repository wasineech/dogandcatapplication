package com.myweb.dogandcatapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(IndexActivity.class)
                .withSplashTimeOut(5000)
                .withBackgroundColor(Color.parseColor("#F58F81"))
//                .withBackgroundResource(R.drawable.gradient1)
                .withLogo(R.drawable.dogcat);

        config.getLogo().setMaxHeight(450);
        config.getLogo().setMaxWidth(450);


        View view = config.create();

        setContentView(view);
    }
}
