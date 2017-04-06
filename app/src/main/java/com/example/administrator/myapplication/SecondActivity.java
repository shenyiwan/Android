package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.BaiduMap;

public class SecondActivity extends AppCompatActivity implements
    View.OnClickListener{

    private MapView mMapView=null;
    private BaiduMap bdMap;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_second);
        init();

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
    }



  private void init(){
    mMapView=(MapView) findViewById(R.id.bmapview);
    bdMap=mMapView.getMap();
    bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    mMapView.onResume();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    mMapView.onPause();
  }

  @Override
    protected void onDestroy()
  {
    mMapView.onDestroy();
    mMapView=null;
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
    }
}
