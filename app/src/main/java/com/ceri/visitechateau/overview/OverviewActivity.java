package com.ceri.visitechateau.overview;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceri.visitechateau.R;
import com.ceri.visitechateau.entities.chateau.Overview;
import com.ceri.visitechateau.entities.chateau.Visit;
import com.ceri.visitechateau.params.AppParams;
import com.ceri.visitechateau.tool.ScreenParam;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Clément
 */
public class OverviewActivity extends AppCompatActivity {
    private Visit visit;
    private Overview overview;
    private ActionBarDrawerToggle m_DrawerToggle;
    private ScreenParam param;

    @Bind(R.id.drawer_layout)
    DrawerLayout m_DrawerLayout;

    @Bind(R.id.toolbar)
    Toolbar m_Toolbar;
    
    @Bind(R.id.overview_picture1)
    ImageView overviewPicture1;

    @Bind(R.id.overview_picture2)
    ImageView overviewPicture2;

    @Bind(R.id.overview_picture3)
    ImageView overviewPicture3;

    @Bind(R.id.overview_title)
    TextView overviewTitle;

    @Bind(R.id.overview_length)
    TextView overviewLength;

    @Bind(R.id.overview_content)
    TextView overviewContent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();
    }

    private void initObjects() {
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
        param = new ScreenParam();
        param.paramWindowFullScreen(getWindow());
        param.paramSetSupportActionBar(m_Toolbar, this);
        m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, 0, 0);

        //Getting the visit
        visit = (Visit) getIntent().getSerializableExtra("Overview");
        //Getting the visit's overview
        overview = visit.getOverview();

        // show the images, 3 needed, but not required
        if(!overview.getImagesContent().isEmpty()) {}
            //layout.setVisibility(View.GONE);
        else
            overviewPicture1.setImageBitmap(BitmapFactory.decodeFile(overview.getImagesContent().get(0).getAbsolutePath()));
        if(overview.getImagesContent().size() > 1)
            overviewPicture2.setImageBitmap(BitmapFactory.decodeFile(overview.getImagesContent().get(1).getAbsolutePath()));
        if(overview.getImagesContent().size() > 2)
            overviewPicture3.setImageBitmap(BitmapFactory.decodeFile(overview.getImagesContent().get(2).getAbsolutePath()));

        if (AppParams.getInstance().getM_french()) {
            overviewTitle.setText(visit.getName());
            overviewLength.setText(overview.readLength_FR());
            overviewContent.setText(overview.readPresentation_FR());
            nameActionBar(visit.getName());
        }
        else {
            overviewTitle.setText(visit.getNameEN());
            overviewLength.setText(overview.readLength_EN());
            overviewContent.setText(overview.readPresentation_EN());
            nameActionBar(visit.getNameEN());
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
