package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sensor = (Button) findViewById(R.id.sensor);
        sensor.setOnClickListener(this);
        Button baidu = (Button) findViewById(R.id.baidu);
        baidu.setOnClickListener(this);
        Button route = (Button) findViewById(R.id.route);
        route.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sensor) {
            Intent intent = new Intent(MainActivity.this, SensorActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.baidu) {
            Intent intent = new Intent(MainActivity.this, TrackShowDemo.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.route) {
            Intent intent = new Intent(MainActivity.this,RoutePlanActivity.class);
            startActivity(intent);
        }
    }

}

