package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;



public class DataActivity extends AppCompatActivity implements
        View.OnClickListener{
    TextView data;
    private Button back,show;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        data = (TextView) findViewById(R.id.data);
        data.setMovementMethod(new ScrollingMovementMethod());
        Button back=(Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        Button show=(Button) findViewById(R.id.show);
        show.setOnClickListener(this);
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
    }
}
