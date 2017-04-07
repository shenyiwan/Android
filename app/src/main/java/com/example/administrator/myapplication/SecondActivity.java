package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class SecondActivity extends AppCompatActivity implements
   View.OnClickListener{

    public MapView mapView=null;
    public BaiduMap baiduMap=null;
    public LocationClient locationClient=null;
    BitmapDescriptor mCurrentMarket=null;
    boolean isFirstLoc=true;

    private  double longitude;
    private  double latitude;
    private MyLocationListener myLitenner = new MyLocationListener();

    private Button back;

   private class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            longitude=location.getLongitude();
            latitude=location.getLatitude();
            boolean isLocateFailed=false;
            if(location==null|mapView==null)
                return;;
            MyLocationData locData=new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);

            if(isFirstLoc){
                isFirstLoc=false;
                LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newLatLngZoom(ll,16);
                baiduMap.animateMapStatus(mapStatusUpdate);
            }
        }

       @Override
       public void onConnectHotSpotMessage(String s, int i) {
       }
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_second);
        initView();
        initData();

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
    }
    private void initData(){
        locationClient.start();;
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setTrafficEnabled(true);
    }

    private void initView(){
      mapView=(MapView) findViewById(R.id.bmapview);
      baiduMap=mapView.getMap();
      baiduMap.setMyLocationEnabled(true);
      locationClient=new LocationClient(getApplicationContext());
      locationClient.registerLocationListener(myLitenner);
        this.setLocationOption();
  }
    @Override
    protected void onResume()
    {
    super.onResume();
    mapView.onResume();
    }

    @Override
    protected void onPause()
    {
    super.onPause();
    mapView.onPause();
    }

    @Override
    protected void onDestroy()
    {
      locationClient.stop();
      baiduMap.setMyLocationEnabled(false);
      super.onDestroy();
      mapView.onDestroy();
      mapView=null;
    }

    private void setLocationOption(){
        LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        locationClient.setLocOption(option);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.back)
        {
            Toast.makeText(SecondActivity.this, "return", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
