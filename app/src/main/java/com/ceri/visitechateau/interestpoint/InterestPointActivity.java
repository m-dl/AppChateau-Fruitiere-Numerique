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
import com.ceri.visitechateau.entities.chateau.InterestPoint;
import com.ceri.visitechateau.params.AppParams;
import com.ceri.visitechateau.tool.ScreenParam;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Maxime
 */
public class InterestPointActivity extends AppCompatActivity {

    private InterestPoint IP;
    private ActionBarDrawerToggle m_DrawerToggle;
    private ScreenParam param;

    @Bind(R.id.drawer_layout)
    DrawerLayout m_DrawerLayout;

    @Bind(R.id.toolbar)
    Toolbar m_Toolbar;

    @Bind(R.id.grid_view)
    GridView gridView;

    @Bind(R.id.interest_point_picture)
    ImageView interestPointPicture;

    @Bind(R.id.interest_point_title)
    TextView interestPointTitle;

    @Bind(R.id.interest_point_content)
    TextView interestPointContent;

    static public String[] pos = null;
    private ArrayList<File> photos = null;
    private ArrayList<Bitmap> myBitmap = null;
    private File tmpFile;
    private Bitmap tmpBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();

        myBitmap = new ArrayList<Bitmap>();

        //Load all the file from the arrayList then convert them into bitmap
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

        //Inflate the grid view with the photos
        gridView.setAdapter(new ImageAdapter(this, pos, myBitmap)); //Pass the Bitmap array

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        interestPointPicture.setImageBitmap(BitmapFactory.decodeFile(IP.getPicture().getAbsolutePath()));

        if (AppParams.getInstance().getM_french()) {
            interestPointTitle.setText(IP.getName());
            interestPointContent.setText(IP.readPresentation_FR());
            nameActionBar(IP.getName());
        }
        else {
            interestPointTitle.setText(IP.getNameEN());
            interestPointContent.setText(IP.readPresentation_EN());
            nameActionBar(IP.getNameEN());
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
