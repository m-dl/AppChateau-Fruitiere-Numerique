package com.ceri.visitechateau.interestpoint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ceri.visitechateau.R;
import com.ceri.visitechateau.entities.chateau.InterestPoint;

import java.io.File;
import java.util.ArrayList;

public class SingleView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);

        Intent i = getIntent();

        int position = i.getExtras().getInt("id");
        final InterestPoint IP = (InterestPoint) getIntent().getSerializableExtra("InterestPoint");
        ArrayList<File> photos = IP.getPhotos();
        File tmpFile;
        tmpFile = photos.get(position);
        Bitmap tmpBitmap;
        tmpBitmap = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
        ImageView imageView = (ImageView) findViewById(R.id.single_view_item);
        imageView.setImageBitmap(tmpBitmap);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
