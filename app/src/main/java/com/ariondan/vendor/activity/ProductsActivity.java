package com.ariondan.vendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ariondan.vendor.R;


public class ProductsActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        findViewById(R.id.button_logout).setOnClickListener(this);
        findViewById(R.id.button_history).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.button_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
    }
}

