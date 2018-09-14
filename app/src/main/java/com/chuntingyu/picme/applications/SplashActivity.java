package com.chuntingyu.picme.applications;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuntingyu.picme.R;
import com.chuntingyu.picme.activities.AlbumActivity;
import com.chuntingyu.picme.tools.KYMath;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(this)
                .withFullScreen()
                .withTargetActivity(AlbumActivity.class)
                .withSplashTimeOut(2000)
                .withBackgroundResource(android.R.color.white)
                .withFooterText("Copyright © 2018, YU Chun-ting\n All rights reserved.")
                .withLogo(R.drawable.ic_color_lens_black_24dp);

        ImageView logo = config.getLogo();
        logo.setColorFilter(Color.parseColor("#80cbc4"));
        int size = Math.round(KYMath.screenSize().x * 0.35f);
        logo.getLayoutParams().width = size;
        logo.getLayoutParams().height = size;

        TextView copyRight = config.getFooterTextView();
        copyRight.setGravity(Gravity.CENTER);
        copyRight.setTextColor(Color.BLACK);
        copyRight.setLineSpacing(0, 1.35f);
        ((RelativeLayout.LayoutParams) copyRight.getLayoutParams()).bottomMargin = KYMath.screenSize().y * 20 / 667;

        //add custom font

//        Typeface pacificoFont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
//        config.getAfterLogoTextView().setTypeface(pacificoFont);

        //change text color

//        config.getHeaderTextView().setTextColor(Color.WHITE);

        //finally create the view

        View easySplashScreenView = config.create();
        setContentView(easySplashScreenView);
    }
}
