package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        SensorEventListener,View.OnClickListener
{
    private SensorManager sensorManager;
    private boolean doWrite=false;
    private Button start,stop,show,map;
    TextView etTxt1,etTxt2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etTxt1=(TextView) findViewById(R.id.shuju);
        etTxt2=(TextView) findViewById(R.id.save);
        etTxt2.setMovementMethod(new ScrollingMovementMethod());
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        Button stop=(Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);
        Button show=(Button) findViewById(R.id.show);
        show.setOnClickListener(this);
        Button map=(Button) findViewById(R.id.map);
        map.setOnClickListener(this);
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


    public void writeFileData(String filename, String message){
        try {
            FileOutputStream fout = openFileOutput(filename, MODE_APPEND);
            byte[]  bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String readFileData(String fileName){
        String result="";
        try {
            FileInputStream fin = openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.start)
        {
            onResume();
            doWrite=true;
        }
        if(view.getId()==R.id.stop)
        {
            onStop();
            doWrite=false;

        }
        if(view.getId()==R.id.show)
        {
            String res=readFileData("acc.txt");
            etTxt2.setText(res);
        }
        if(view.getId()==R.id.map)
        {
            Toast.makeText(MainActivity.this, "open map", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String message=new String();
        float xValue=event.values[0];
        float yValue=event.values[1];
        float zValue=event.values[2];
        DecimalFormat df=new DecimalFormat("#,##0.000");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String str=sdf.format(new Date());
        message =str+"\n";
        message +=df.format(xValue)+" ";
        message +=df.format(yValue)+" ";
        message +=df.format(zValue)+" ";
        etTxt1.setText(message+"\n\n");
        if(doWrite){
            writeFileData("acc.txt",message);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

