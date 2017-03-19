package com.example.administrator.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener
{
    SensorManager sensorManager;
    StringBuilder sb = new StringBuilder();
    TextView etTxt1;
   int k=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etTxt1=(TextView) findViewById(R.id.shuju);
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        Button button = (Button) findViewById(R.id.show);
        button.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected  void onStop()
    {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        k++;
        if (k/2==0)
            onStop();
        else
            onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xValue=event.values[0];
        float yValue=event.values[1];
        float zValue=event.values[2];
            etTxt1.setText("x轴： "+xValue+"\n"+"  y轴： "+yValue+"\n"+"  z轴： "+zValue);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }


