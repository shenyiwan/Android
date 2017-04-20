package com.example.administrator.myapplication;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements View.OnClickListener,SensorEventListener{


    public LocationClient locationClient=null;
    private MyLocationListener myListenner = new MyLocationListener();
    BitmapDescriptor mCurrentMarket=null;
    OverlayOptions options;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private SensorManager mSensorManager;
    private MyLocationData locData;
    private Double lastX = 0.0;

    private int mCurrentDirection = 0;
    private float mCurrentAccracy;
    private  String currentAddr;
    private  double longitude;
    private  double latitude;
    boolean isFirstLoc=true;
    public MapView mapView=null;
    public BaiduMap baiduMap=null;

    private Button back,start,stop,clear,route;

    List<LatLng> points =new ArrayList<LatLng>();
    List<LatLng> points_tem=new ArrayList<LatLng>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_route);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initLocate();

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        start=(Button)findViewById(R.id.start);
        start.setOnClickListener(this);
        stop=(Button)findViewById(R.id.stop);
        stop.setOnClickListener(this);
        route=(Button)findViewById(R.id.route);
        route.setOnClickListener(this);
        clear=(Button)findViewById(R.id.clear);
        clear.setOnClickListener(this);
    }


    private void initLocate(){
        mapView=(MapView) findViewById(R.id.bmapview);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListenner);

        LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.start();


        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarket));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(latitude)
                    .longitude(longitude).build();
            baiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if(location.getLocType()== BDLocation.TypeGpsLocation){// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());// 单位度
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//            }else if(location.getLocType()== BDLocation.TypeNetWorkLocation){// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            }else if(location.getLocType()== BDLocation.TypeServerError){
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败");
//            }else if(location.getLocType()== BDLocation.TypeNetWorkException){
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            }else if(location.getLocType()== BDLocation.TypeCriteriaException){
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因,可以试着重启手机"

            if (location == null || mapView == null){
                return;
            }
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            mCurrentAccracy=location.getRadius();
            currentAddr = location.getAddrStr();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);

            LatLng point = new LatLng(latitude, longitude);
            points.add(point);

            if (isFirstLoc) {
                Toast.makeText(RouteActivity.this, "当前位置" + currentAddr + "经度" + longitude + "纬度" + latitude, Toast.LENGTH_SHORT).show();
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                locationClient.stop();
            }

            if (points.size() == 2) {
                // 这里绘制起点
                drawStart(points);
            } else if (points.size() > 5) {
                points_tem = points.subList(points.size() - 4, points.size());
                options = new PolylineOptions().color(0xAAFF0000).width(6)
                        .points(points_tem);
                baiduMap.addOverlay(options);
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i){
        }
    }

    public void drawStart(List<LatLng> points2){
        double myLat=0.0;
        double myLng=0.0;
        for(LatLng ll:points2){
            myLat+=ll.latitude;
            myLng+=ll.longitude;
        }
        LatLng avePoint=new LatLng(myLat/points2.size(),myLng/points2.size());
        points.add(avePoint);
        options=new DotOptions().center(avePoint).color(0xAA00ff00)
                .radius(15);
        baiduMap.addOverlay(options);
    }

    protected void drawEnd(List<LatLng> points2){
        double myLat=0.0;
        double myLng=0.0;
        if(points2.size()>2){
            for(int i=points2.size()-2;i<points2.size();i++){
                LatLng ll=points2.get(i);
                myLat+=ll.latitude;
                myLng+=ll.longitude;
            }
            LatLng avePoint=new LatLng(myLat/2,myLng/2);
            options=new DotOptions().center(avePoint).color(0xAAff00ff)
                    .radius(15);
            baiduMap.addOverlay(options);
        }
    }

    @Override
    protected void onPause()
    {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        mapView.onResume();
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onStop()
    {
        locationClient.stop();
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        locationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView=null;
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.back) {
            Toast.makeText(RouteActivity.this, "return", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RouteActivity.this, MainActivity.class);
            startActivity(intent);
        }

        if(view.getId()==R.id.start) {
            if(!locationClient.isStarted()){
                locationClient.start();
            }
            points.clear();
        }

        if(view.getId()==R.id.stop){
            drawEnd(points);
            if(locationClient.isStarted()){
                locationClient.stop();
            }
        }

        if(view.getId()==R.id.route){
            Intent intent = new Intent(RouteActivity.this, RoutePlanActivity.class);
            startActivity(intent);
        }

        if (view.getId() == R.id.clear) {
            baiduMap.clear();
        }
    }
}
