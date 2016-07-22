package com.example.android_mvvm.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        setTitle("Item Details");
    }
}
