package com.ceri.visitechateau.tool;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ScreenParam {

    // --- set screen properties for design purposes ---

    public void paramWindow(Window window) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.setExitTransition(new Explode());
    }

    public void paramWindowTranslucent(Window window) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.requestFeature(Window.FEATURE_ACTION_BAR);
        window.setExitTransition(new Explode());
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void paramWindowFullScreen(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void paramWindowFullScreenNoUIBar(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        window.setExitTransition(new Explode());
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void paramHideActionBar(android.support.v7.app.ActionBar actionBar) {
        actionBar.hide();
    }

    public void paramSetSupportActionBar(Toolbar m_toolbar, AppCompatActivity activity) {
        activity.setSupportActionBar(m_toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void paramSetCollapsingActionBar(CollapsingToolbarLayout m_CollapsingToolbarLayout, String name, String RGB, String textColor) {
        m_CollapsingToolbarLayout.setTitle(name);
        m_CollapsingToolbarLayout.setContentScrimColor(Color.parseColor(RGB));
        if (textColor != null)
            m_CollapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor(textColor));
    }
}