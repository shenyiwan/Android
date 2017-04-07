package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;



public class SplashActivity extends Activity {

    //加载欢迎界面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);      //设置无标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 为了减少代码使用匿名Handler创建一个延时的调用
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //通过Intent打开最终真正的主界面Main这个Activity
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                //启动Main界面
                SplashActivity.this.startActivity(mainIntent);
                //关闭自己这个开场屏
                SplashActivity.this.finish();
            }
        },1500);
    }
}

