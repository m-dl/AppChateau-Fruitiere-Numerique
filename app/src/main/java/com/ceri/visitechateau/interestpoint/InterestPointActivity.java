package com.ceri.visitechateau.interestpoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ceri.visitechateau.entities.chateau.InterestPoint;

/**
 * Created by Maxime
 */
public class InterestPointActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        InterestPoint IP = (InterestPoint) getIntent().getSerializableExtra("InterestPoint");
    }
}
