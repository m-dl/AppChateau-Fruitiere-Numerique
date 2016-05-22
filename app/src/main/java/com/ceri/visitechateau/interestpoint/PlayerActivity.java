package com.ceri.visitechateau.interestpoint;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.ceri.visitechateau.R;
import com.ceri.visitechateau.entities.chateau.InterestPoint;
import com.ceri.visitechateau.params.AppParams;
import com.ceri.visitechateau.tool.ScreenParam;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    private ActionBarDrawerToggle m_DrawerToggle;
    private ScreenParam param;

    @Bind(R.id.drawer_layout)
    DrawerLayout m_DrawerLayout;

    @Bind(R.id.toolbar)
    Toolbar m_Toolbar;

    @Bind(R.id.surfaceViewVideo)
    SurfaceView pre;

    SurfaceHolder holder;
    MediaPlayer mdP;
    File tmpFile;
    private MediaController mcontrol;
    private Handler handler;
    private InterestPoint IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();

        handler = new Handler();
        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        holder = pre.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);
        this.tmpFile = IP.getVideos().get(position);
    }

    private void initObjects() {
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        param = new ScreenParam();
        param.paramWindowFullScreen(getWindow());
        param.paramSetSupportActionBar(m_Toolbar, this);
        m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, 0, 0);
        IP = (InterestPoint) getIntent().getSerializableExtra("InterestPoint");

        if (AppParams.getInstance().getM_french()) {
            nameActionBar(IP.getName());
        }
        else {
            nameActionBar(IP.getNameEN());
        }
    }

    private void nameActionBar(String s) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(s);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void computeSize() {
        int mVideoWidth = this.mdP.getVideoWidth();
        int mVideoHeight = this.mdP.getVideoHeight();
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        if(holder == null || pre == null)
            return;

        // get screen size
        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();

        // getWindow().getDecorView() doesn't always take orientation into
        // account, we have to correct the values
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        // force surface buffer size
        holder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        ViewGroup.LayoutParams lp = pre.getLayoutParams();
        lp.width = w;
        lp.height = h;
        pre.setLayoutParams(lp);
        pre.invalidate();
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
    public void surfaceCreated(SurfaceHolder holder) {
        String path = tmpFile.getAbsolutePath();
        this.mdP = new MediaPlayer();
        this.mcontrol = new MediaController(this);
        try {
            this.mdP.setDataSource(path);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mdP.setDisplay(this.holder);

        try {
            this.mdP.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mdP.setOnPreparedListener(this);
        pre.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    mcontrol.show();
                    return true;
                }
                return false;
            }
        });

        computeSize();
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mdP.stop();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mcontrol.setMediaPlayer(this);
        mcontrol.setAnchorView(findViewById(R.id.surfaceViewVideo));

        handler.post(new Runnable() {
            @Override
            public void run() {
                mcontrol.setEnabled(true);
                mcontrol.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mdP.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mcontrol.show();
        return super.onTouchEvent(event);
    }

    // Media player control
    @Override
    public void start() {
        if(this.mdP != null) {
            this.mdP.start();
        }
    }

    @Override
    public void pause() {
        if(this.mdP != null) {
            this.mdP.pause();
        }
    }

    @Override
    public int getDuration() {
        if(this.mdP != null) {
            return this.mdP.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(this.mdP.isPlaying()) {
            return this.mdP.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if(this.mdP != null) {
            this.mdP.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        if(this.mdP != null) {
            return this.mdP.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
