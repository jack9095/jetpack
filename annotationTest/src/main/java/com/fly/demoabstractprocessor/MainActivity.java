package com.fly.demoabstractprocessor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fly.TestAnnotation;
import com.fly.demoprocessor.auto.fly$$Fly;

@TestAnnotation(
        name = "fly",
        text = "Hello !!! Welcome "
)
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void changeText(View view) {
        fly$$Fly fly = new fly$$Fly();
        String message = fly.getMessage();
        ((TextView)view).setText(message);
    }
}
