package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {
    Animation topAnimation, bottomAnimation;
    CircleImageView imagLogo;
    TextView txtTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        topAnimation= AnimationUtils.loadAnimation(SplashActivity.this,R.anim.top_animation);
        bottomAnimation=AnimationUtils.loadAnimation(SplashActivity.this,R.anim.bottom_animation);
        imagLogo=findViewById(R.id.imgLogo);
        txtTrip=findViewById(R.id.txtTrip);
        imagLogo.setAnimation(topAnimation);
        txtTrip.setAnimation(bottomAnimation);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}