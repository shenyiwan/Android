package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.B;

import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;


public class DataActivity extends AppCompatActivity implements
        View.OnClickListener{
    private TextView data;
    private Button back,show,clear;
    private File file=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        data = (TextView) findViewById(R.id.data);
        data.setMovementMethod(new ScrollingMovementMethod());
        Button back=(Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        Button show=(Button) findViewById(R.id.show);
        show.setOnClickListener(this);
        Button clear=(Button) findViewById(R.id.clear);
        clear.setOnClickListener(this);
    }

    public String readFileData(String filename){
        String result="";
        try {
            file=new File(Environment.getExternalStorageDirectory().toString()+File.separator+filename);
            FileInputStream fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void writeFileData(String filename, String message) {
        try {
            file=new File(Environment.getExternalStorageDirectory().toString()+File.separator+filename);
            FileOutputStream fout = new FileOutputStream(file,false);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view){
        if(view.getId()==R.id.back)
        {
            Toast.makeText(DataActivity.this, "return", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DataActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.show){
            String res=readFileData("acc.txt");
            data.setText(res);
        }
        if(view.getId()==R.id.clear)
        {
            writeFileData("acc.txt","");
            String res=readFileData("acc.txt");
            data.setText(res);
        }
    }
}
