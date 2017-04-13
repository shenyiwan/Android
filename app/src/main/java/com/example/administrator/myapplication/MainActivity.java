package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;


import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class MainActivity extends AppCompatActivity implements
        SensorEventListener,View.OnClickListener {
    private SensorManager sensorManager;
    private boolean flag = true;
    private Button start, stop, show, map;
    TextView etTxt1;
    private float xValue, yValue, zValue;
    private int j=0;


    private LineChartView lineChart1;
    private LineChartView lineChart2;
    private LineChartView lineChart3;

    private List<PointValue> xPointValues = new ArrayList<PointValue>();
    private List<PointValue> yPointValues = new ArrayList<PointValue>();
    private List<PointValue> zPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    String[] date =new String[100];
    int[] score=new  int[100];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        etTxt1 = (TextView) findViewById(R.id.shuju);
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);

//        long Time= System.currentTimeMillis();
//        int savedTime=(int)Time;

        lineChart1=(LineChartView) findViewById(R.id.chart1);
        lineChart2=(LineChartView) findViewById(R.id.chart2);
        lineChart3=(LineChartView) findViewById(R.id.chart3);


        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);
        Button show = (Button) findViewById(R.id.show);
        show.setOnClickListener(this);
        Button map = (Button) findViewById(R.id.map);
        map.setOnClickListener(this);
    }


    private void initLineChart(LineChartView lineChart,List<PointValue> pointValues,int color){
          Line line=new Line(pointValues).setColor(color).setStrokeWidth(2);
          List<Line> lines=new ArrayList<Line>();
          line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
          line.setCubic(true);//曲线是否平滑，即是曲线还是折线
          line.setFilled(false);//是否填充曲线的面积
          line.setHasLabels(false);//曲线的数据坐标是否加上备注
//        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
          line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
          line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
          lines.add(line);
          LineChartData data = new LineChartData();
          data.setLines(lines);

          Axis axisX=new Axis();
          axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
          axisX.setTextColor(Color.WHITE);  //设置字体颜色
          axisX.setTextColor(Color.parseColor("#336699"));
          axisX.setName("加速度值");  //表格名称
          axisX.setTextSize(10);//设置字体大小
          axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
          axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
          data.setAxisXBottom(axisX); //x 轴在底部
          //data.setAxisXTop(axisX);  //x 轴在顶部
          axisX.setHasLines(true); //x 轴分割线

          Axis axisY = new Axis();  //Y轴
          axisY.setName("m/s^2");//y轴标注
          axisY.setTextSize(10);//设置字体大小
          axisY.setTextColor(Color.WHITE);  //设置字体颜色
          axisY.setTextColor(Color.parseColor("#336699"));
          data.setAxisYLeft(axisY);  //Y轴设置在左边

          //设置行为属性，支持缩放、滑动以及平移
          lineChart.setInteractive(true);
          lineChart.setZoomType(ZoomType.HORIZONTAL);
          lineChart.setMaxZoom((float) 2);//最大方法比例
          lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
          lineChart.setLineChartData(data);
          lineChart.setVisibility(View.VISIBLE);

          Viewport v=new Viewport(lineChart.getMaximumViewport());
          v.left=0;
          lineChart.setCurrentViewport(v);

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
            flag = true;
        }
        if (view.getId() == R.id.stop) {
            onStop();
            flag = false;
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
        if (flag) {
            writeFileData("acc.txt", message);

            mAxisXValues.add(new AxisValue(j).setLabel(String.valueOf(j)));
            xPointValues.add(new PointValue(j,xValue));
            initLineChart(lineChart1,xPointValues,Color.rgb(137, 230, 81));
            mAxisXValues.add(new AxisValue(j).setLabel(String.valueOf(j)));
            yPointValues.add(new PointValue(j,yValue));
            initLineChart(lineChart2,yPointValues,Color.rgb(240, 240, 30));
            mAxisXValues.add(new AxisValue(j).setLabel(String.valueOf(j)));
            zPointValues.add(new PointValue(j,zValue));
            initLineChart(lineChart3,zPointValues,Color.rgb(89, 199, 250));
            j++;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

