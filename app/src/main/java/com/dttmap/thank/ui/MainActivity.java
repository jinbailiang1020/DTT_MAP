package com.dttmap.thank.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.dttmap.thank.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View iv = findViewById(R.id.iv);
        Animation aa = new RotateAnimation(0.0f, +350.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        aa.setDuration(1000);
        iv.startAnimation(aa);
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(MainActivity.this,LoadDataActivity.class));
                startActivity(new Intent(MainActivity.this,MapActivity.class));
                finish();
            }
        },1000);
    }
}
