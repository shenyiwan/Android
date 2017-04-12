package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        SensorEventListener,View.OnClickListener {
    private SensorManager sensorManager;
    private boolean doWrite = true;
    private Button start, stop, show, map;
    TextView etTxt1;
    private float xValue, yValue, zValue;
    private float oxValue=0;
    private float oyValue=0;
    private float ozValue=0;
    private int x = 0;

    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private Paint mPaint = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etTxt1 = (TextView) findViewById(R.id.shuju);
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);
        Button show = (Button) findViewById(R.id.show);
        show.setOnClickListener(this);
        Button map = (Button) findViewById(R.id.map);
        map.setOnClickListener(this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceView.setZOrderOnTop(true);
        mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new MyHolder());

//        init();
    }


    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }


    public void writeFileData(String filename, String message) {
        try {
            FileOutputStream fout = openFileOutput(filename, MODE_APPEND);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.start) {
            onResume();
            doWrite = true;
        }
        if (view.getId() == R.id.stop) {
            onStop();
            doWrite = false;
        }
        if (view.getId() == R.id.show) {
            Toast.makeText(MainActivity.this, "show data", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DataActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.map) {
            Toast.makeText(MainActivity.this, "open map", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String message = new String();
        xValue = event.values[0];
        yValue = event.values[1];
        zValue = event.values[2];
        DecimalFormat df = new DecimalFormat("#,##0.000");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String str = sdf.format(new Date());
        message = str + "\n";
        message += df.format(xValue) + " ";
        message += df.format(yValue) + " ";
        message += df.format(zValue) + " ";
        etTxt1.setText(message + "\n");
        if (doWrite) {
            writeFileData("acc.txt", message);
        }

//        Canvas mCanvas = mSurfaceHolder.lockCanvas();
//        DisplayMetrics dm=new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int height=dm.heightPixels;
//        int width=dm.widthPixels;
//        mPaint.setColor(Color.BLACK);
//        mCanvas.drawLine(0,0,0,height,mPaint);
//        mCanvas.drawLine(0,50,width,50,mPaint);
//        mCanvas.drawLine(0,150,width,150,mPaint);
//        mCanvas.drawLine(0,250,width,250,mPaint);
//        try {
//            if (mCanvas != null) {
//                mPaint.setColor(Color.RED);
//                //mCanvas.drawPoint(x,50 + xValue,mPaint);
//                mCanvas.drawLine(x,50-oxValue,x+1,50-xValue,mPaint);
//                mPaint.setColor(Color.GREEN);
//                //mCanvas.drawPoint(x,150 + yValue,mPaint);
//                mCanvas.drawLine(x,150-oyValue,x+1,150-yValue,mPaint);
//                mPaint.setColor(Color.BLUE);
//                //mCanvas.drawPoint(x,250 + zValue,mPaint);
//                mCanvas.drawLine(x,250-ozValue,x+1,250-zValue,mPaint);
//                x++;
//                oxValue=xValue;
//                oyValue=yValue;
//                ozValue=zValue;
//                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
//                if (x > width) {
//                    x = 0;
//                    mCanvas=mSurfaceHolder.lockCanvas(new Rect(0,height,width,0));
//                    mCanvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
//                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (mCanvas != null) {
//                mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
//                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
//            }
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }



    public class MyHolder implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }
}

