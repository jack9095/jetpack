package com.fly.demoabstractprocessor;

import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fly.TestAnnotation;
import com.fly.demoprocessor.auto.fly$$Fly;

@TestAnnotation(
        name = "fly",
        text = "Hello !!! Welcome "
)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        View decorView = getWindow().getDecorView();

        Log.e("decorView = ",decorView + "");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart = ","onStart");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.e("onPostCreate = ","onPostCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestart = ","onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume = ","onResume");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("onPostResume = ","onPostResume");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("onAttachedToWindow = ","onAttachedToWindow");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("onSaveInstanceState = ","onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause = ","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop = ","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy = ","onDestroy");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("onDetachedFromWindow = ","onDetachedFromWindow");
    }



    public void changeText(View view) {
        fly$$Fly fly = new fly$$Fly();
        String message = fly.getMessage();
        ((TextView) view).setText(message);
    }
}
