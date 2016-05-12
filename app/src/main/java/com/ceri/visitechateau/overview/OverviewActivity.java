package com.ceri.visitechateau.overview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ceri.visitechateau.entities.chateau.Visit;

/**
 * Created by Maxime
 */
public class OverviewActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        Visit visit = (Visit) getIntent().getSerializableExtra("Overview");

    }
}
