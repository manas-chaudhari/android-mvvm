package com.example.android_mvvm.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android_mvvm.sample.adapters.ItemListActivity;
import com.example.android_mvvm.sample.functional.DataLoadingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, DataLoadingActivity.class));
    }
}
