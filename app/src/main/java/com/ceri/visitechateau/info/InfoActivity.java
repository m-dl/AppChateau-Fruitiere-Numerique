package com.ceri.visitechateau.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceri.visitechateau.R;
import com.ceri.visitechateau.entities.chateau.Info;
import com.ceri.visitechateau.params.AppParams;
import com.ceri.visitechateau.tool.ImageAdapter;
import com.ceri.visitechateau.tool.ScreenParam;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Clément
 */
public class InfoActivity extends AppCompatActivity {
    private Info info;
    private ActionBarDrawerToggle m_DrawerToggle;
    private ScreenParam param;

    @Bind(R.id.drawer_layout)
    DrawerLayout m_DrawerLayout;

    @Bind(R.id.toolbar)
    Toolbar m_Toolbar;

    @Bind(R.id.grid_view_photo)
    GridView gridViewPhoto;

    @Bind(R.id.info_picture)
    ImageView infoPicture;

    @Bind(R.id.info_title)
    TextView infoTitle;

    @Bind(R.id.info_content)
    TextView infoContent;

    @Bind(R.id.info_photo_title)
    TextView infoPhotoTitle;
    
    static public String[] pos;
    private ArrayList<File> photos;
    private ArrayList<Bitmap> myBitmap;
    private File tmpFile;
    private Bitmap tmpBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(AppParams.getInstance().getCurrentVisit() == null) {
            finish();
            return;
        }

        initObjects();

        //Inflate the grid view with the photos
        gridViewPhoto.setAdapter(new ImageAdapter(this, pos, myBitmap)); //Pass the Bitmap array

        gridViewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myI = new Intent(getApplicationContext(), SingleView.class);
                myI.putExtra("id", position);
                myI.putExtra("Info", info);
                startActivity(myI);
            }
        });
    }

    private void initObjects() {
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        param = new ScreenParam();
        param.paramWindowFullScreen(getWindow());
        param.paramSetSupportActionBar(m_Toolbar, this);
        m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, 0, 0);

        //Getting the visit info
        info = AppParams.getInstance().getCurrentVisit().getInfo();
        //Getting the arraylist of picture of this info
        photos = info.getPhotos();

        if(info.getPicture() != null)
            infoPicture.setImageBitmap(BitmapFactory.decodeFile(info.getPicture().getAbsolutePath()));

        if (AppParams.getInstance().getM_french()) {
            infoTitle.setText(AppParams.getInstance().getCurrentVisit().getName());
            infoContent.setText(info.readContent_FR());
            infoPhotoTitle.setText(R.string.images);
            nameActionBar(AppParams.getInstance().getCurrentVisit().getName());
        }
        else {
            infoTitle.setText(AppParams.getInstance().getCurrentVisit().getNameEN());
            infoContent.setText(info.readContent_EN());
            infoPhotoTitle.setText(R.string.pictures);
            nameActionBar(AppParams.getInstance().getCurrentVisit().getNameEN());
        }

        // if some media elements are empty, do not dislay titles and gridviews
        if(info.getPhotos().isEmpty()) {
            gridViewPhoto.setVisibility(View.GONE);
            infoPhotoTitle.setVisibility(View.GONE);
        }

        myBitmap = new ArrayList<Bitmap>();
        // Load all the file from the arrayList then convert them into bitmap
        if(!photos.isEmpty()){
            pos = new String[photos.size()];
            for(int i=0; i<photos.size(); i++){
                pos[i]="media"+i;
                this.tmpFile = photos.get(i);
                if(tmpFile != null){
                    //Decode the file into a bitmap
                    tmpBitmap = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
                    //Put the created bitmap into an array to be pass to the ImageAdapter
                    if(tmpBitmap != null){
                        this.myBitmap.add(tmpBitmap);
                    }
                }
            }
        }
    }

    private void nameActionBar(String s) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(s);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
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
