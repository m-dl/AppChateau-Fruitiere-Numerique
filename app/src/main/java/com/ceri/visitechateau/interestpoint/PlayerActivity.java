package com.ceri.visitechateau.interestpoint;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import com.ceri.visitechateau.R;
import com.ceri.visitechateau.entities.chateau.InterestPoint;

import java.io.File;
import java.io.IOException;

public class PlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    SurfaceView pre;
    SurfaceHolder holder;
    MediaPlayer mdP;
    File tmpFile;
    private int currentP;
    private MediaController mcontrol;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        currentP = 0;
        handler = new Handler();
        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        final InterestPoint IP = (InterestPoint) getIntent().getSerializableExtra("InterestPoint");
        pre = (SurfaceView) findViewById(R.id.surfaceViewVideo);
        holder = pre.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);
        this.tmpFile = IP.getVideos().get(position);
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
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mdP.stop(); //Need verif @ next iteration
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.currentP = this.mdP.getCurrentPosition();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentPosition",this.currentP);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.currentP = savedInstanceState.getInt("CurrentPosition");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mcontrol.show();
        return super.onTouchEvent(event);
    }

    // Media player control
    @Override
    public void start() {
        this.mdP.start();
    }

    @Override
    public void pause() {
        this.mdP.pause();
    }

    @Override
    public int getDuration() {
        return this.mdP.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return this.mdP.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        this.mdP.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return this.mdP.isPlaying();
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
