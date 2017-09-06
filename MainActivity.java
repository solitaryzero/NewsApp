package com.example.sam.newapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btOnClick(View v) {
        Intent intent = new Intent(MainActivity.this, ShowDetails.class);
        intent.putExtra("Headline", "This is a headline");
        intent.putExtra("Details", "This is a bunch of details");
        startActivity(intent);
    }
}
