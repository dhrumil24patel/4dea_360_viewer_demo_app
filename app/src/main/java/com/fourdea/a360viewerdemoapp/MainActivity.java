package com.fourdea.a360viewerdemoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MyPanoramaHelper myPanoramaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPanoramaHelper = new MyPanoramaHelper(MainActivity.this);
        myPanoramaHelper.initialize();
    }

    public void start(View view){
        myPanoramaHelper.startAutoPlay();
    }

    public void stop(View view){
        myPanoramaHelper.stopAutoPlay();
    }

    public void cardBoard(View view){
        myPanoramaHelper.goToCardBoardMode();
    }

    public void gyroToggle(View view){
        if(myPanoramaHelper.isGyroOn()){
            myPanoramaHelper.turnGyroOff();
        }
        else{
            myPanoramaHelper.turnGyroOn();
        }
    }
}
