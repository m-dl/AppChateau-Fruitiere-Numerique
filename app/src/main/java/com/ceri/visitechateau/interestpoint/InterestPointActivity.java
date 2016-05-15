package com.ceri.visitechateau.interestpoint;

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
import com.ceri.visitechateau.activities.SingleView;
import com.ceri.visitechateau.entities.chateau.InterestPoint;
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
public class InterestPointActivity extends AppCompatActivity {
    private InterestPoint IP;
    private ActionBarDrawerToggle m_DrawerToggle;
    private ScreenParam param;

    @Bind(R.id.drawer_layout)
    DrawerLayout m_DrawerLayout;

    @Bind(R.id.toolbar)
    Toolbar m_Toolbar;

    @Bind(R.id.grid_view_photo)
    GridView gridViewPhoto;

    @Bind(R.id.grid_view_interieur)
    GridView gridViewInterieur;

    @Bind(R.id.grid_view_360)
    GridView gridView360;

    @Bind(R.id.grid_view_video)
    GridView gridViewVideo;

    @Bind(R.id.interest_point_picture)
    ImageView interestPointPicture;

    @Bind(R.id.interest_point_title)
    TextView interestPointTitle;

    @Bind(R.id.interest_point_content)
    TextView interestPointContent;

    @Bind(R.id.interest_point_photo_title)
    TextView interestPointPhotoTitle;

    @Bind(R.id.interest_point_interieur_title)
    TextView interestPointInterieurTitle;

    @Bind(R.id.interest_point_360_title)
    TextView interestPoint360Title;

    @Bind(R.id.interest_point_video_title)
    TextView interestPointVideoTitle;

    static public String[] pos;
    private ArrayList<File> photos;
    private ArrayList<Bitmap> myBitmap;
    private File tmpFile;
    private Bitmap tmpBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();

        //Inflate the grid view with the photos
        gridViewPhoto.setAdapter(new ImageAdapter(this, pos, myBitmap)); //Pass the Bitmap array

        gridViewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myI = new Intent(getApplicationContext(), SingleView.class);
                myI.putExtra("id", position);
                myI.putExtra("InterestPoint", IP);
                startActivity(myI);
            }
        });
    }

    private void initObjects() {
        setContentView(R.layout.activity_interest_point);
        ButterKnife.bind(this);
        param = new ScreenParam();
        param.paramWindowFullScreen(getWindow());
        param.paramSetSupportActionBar(m_Toolbar, this);
        m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, 0, 0);

        //Getting the interest point
        IP = (InterestPoint) getIntent().getSerializableExtra("InterestPoint");
        //Getting the arraylist of picture of this IP
        photos = IP.getPhotos();

        if(IP.getPicture() != null)
            interestPointPicture.setImageBitmap(BitmapFactory.decodeFile(IP.getPicture().getAbsolutePath()));

        if (AppParams.getInstance().getM_french()) {
            interestPointTitle.setText(IP.getName());
            interestPointContent.setText(IP.readPresentation_FR());
            interestPointPhotoTitle.setText(R.string.images);
            interestPointInterieurTitle.setText(R.string.interieur);
            interestPoint360Title.setText(R.string.images360);
            interestPointVideoTitle.setText(R.string.videos);
            nameActionBar(IP.getName());
        }
        else {
            interestPointTitle.setText(IP.getNameEN());
            interestPointContent.setText(IP.readPresentation_EN());
            interestPointPhotoTitle.setText(R.string.pictures);
            interestPointInterieurTitle.setText(R.string.inside);
            interestPoint360Title.setText(R.string.pictures360);
            interestPointVideoTitle.setText(R.string.videosen);
            nameActionBar(IP.getNameEN());
        }

        // if some media elements are empty, do not dislay titles and gridviews
        if(IP.getPhotos().isEmpty()) {
            gridViewPhoto.setVisibility(View.GONE);
            interestPointPhotoTitle.setVisibility(View.GONE);
        }

        if(IP.get_360().isEmpty()) {
            gridView360.setVisibility(View.GONE);
            interestPoint360Title.setVisibility(View.GONE);
        }

        if(IP.getInterieur().isEmpty()) {
            gridViewInterieur.setVisibility(View.GONE);
            interestPointInterieurTitle.setVisibility(View.GONE);
        }

        if(IP.getVideos().isEmpty()) {
            gridViewVideo.setVisibility(View.GONE);
            interestPointVideoTitle.setVisibility(View.GONE);
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
