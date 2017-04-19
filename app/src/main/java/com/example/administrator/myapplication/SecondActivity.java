package com.example.administrator.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsSatellite;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
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
import java.util.logging.LogRecord;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{

    public MapView mapView=null;
    public BaiduMap baiduMap=null;
    public LocationClient locationClient=null;
    BitmapDescriptor mCurrentMarket=null;
    OverlayOptions options;
    boolean isFirstLoc=true;

    private MyLocationListener myListenner = new MyLocationListener();

    private Button back,start,stop,route;
    private  String currentAddr;
    private  double longitude;
    private  double latitude;

    List<LatLng> points =new ArrayList<LatLng>();
    List<LatLng> points_tem=new ArrayList<LatLng>();

    Handler handler=new Handler();
    boolean isStopLocClient=false;

    private class MyLocationListener implements BDLocationListener{
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
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            String currentAddr = location.getAddrStr();//位置
            if (location == null || mapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            LatLng point=new LatLng(latitude,longitude);
            points.add(point);

            if (isFirstLoc) {
                Toast.makeText(SecondActivity.this, "当前位置"+currentAddr+"经度"+longitude+"纬度"+latitude, Toast.LENGTH_SHORT).show();
                points.add(point);
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
                baiduMap.animateMapStatus(mapStatusUpdate);
            }

            if(points.size()==5){
                drawStart(points);
            }else if(points.size()>7){
                points_tem=points.subList(points.size()-4,points.size());
                options=new PolylineOptions().color(0xAAFF0000).width(6)
                        .points(points_tem);
                baiduMap.addOverlay(options);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_second);
        handler.postDelayed(new MyRunable(),3000);
        initView();
        initData();
        TextView txt=(TextView)findViewById(R.id.text2);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        Button start=(Button)findViewById(R.id.start);
        start.setOnClickListener(this);
        Button stop=(Button)findViewById(R.id.stop);
        stop.setOnClickListener(this);
        Button route=(Button)findViewById(R.id.route);
        route.setOnClickListener(this);

    }

    private void initData(){
        locationClient.start();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setTrafficEnabled(true);
    }

    private void initView(){
        mapView=(MapView) findViewById(R.id.bmapview);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListenner);
        this.setLocationOption();

    }

    private void setLocationOption(){
        LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }

    private class MyRunable implements Runnable{

        public void run(){
            if(!locationClient.isStarted()){
                locationClient.start();
            }
            if(!isStopLocClient){
                handler.postDelayed(this,3000);
            }
        }
    }


    public void drawStart(List<LatLng> points2){
        double myLat=0.0;
        double myLng=0.0;
        for(LatLng ll : points2){
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
        if(points2.size()>5){
            for(int i=points2.size()-5;i<points2.size();i++){
                LatLng ll=points2.get(i);
                myLat+=ll.latitude;
                myLng+=ll.longitude;
            }
            LatLng avePoint=new LatLng(myLat/5,myLng/5);
            options=new DotOptions().center(avePoint).color(0xAAff00ff)
                    .radius(15);
            baiduMap.addOverlay(options);
        }
    }

    protected void drawMyRoute(List<LatLng> points2){
        OverlayOptions options=new PolylineOptions().color(0xAAFF0000)
                .width(10).points(points2);
        baiduMap.addOverlay(options);
    }

    @Override
    protected void onStop(){
        isStopLocClient=true;
        locationClient.stop();
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        locationClient.stop();
        isStopLocClient=true;
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView=null;
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.back)
        {
            Toast.makeText(SecondActivity.this, "return", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
        }

        if(view.getId()==R.id.start)
        {
            isStopLocClient=false;
            if(!locationClient.isStarted()){
                locationClient.start();
            }
        }

        if(view.getId()==R.id.stop){
            isStopLocClient=true;
            if(locationClient.isStarted()){
                drawEnd(points);
                locationClient.stop();
            }
        }

        if(view.getId()==R.id.route){
            Intent intent = new Intent(SecondActivity.this, RoutePlanDemo.class);
            startActivity(intent);
        }
    }
}
