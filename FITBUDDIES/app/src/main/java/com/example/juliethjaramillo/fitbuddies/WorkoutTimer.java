package com.example.juliethjaramillo.fitbuddies;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by juliethjaramillo on 2/12/17.
 */

public class WorkoutTimer  extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

     /**   new CountDownTimer(1000*60*30,1000){
            public void onTick(long millisUntilFinished){
                ((TextView) findViewById(R.id.textView9)).setText(
                        millisUntilFinished/600000 + ":" + (millisUntilFinished/10000)%60
                );
            }

            @Override
            public void onFinish() {

            }
        }.start(); **/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

}
