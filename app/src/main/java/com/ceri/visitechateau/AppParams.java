package com.ceri.visitechateau;

import android.support.v4.app.FragmentManager;

/**
 * Created by Maxime
 */
public class AppParams {
    public static AppParams instance;
    private FragmentManager m_FragmentManager;
    private boolean m_fragment;

    public static AppParams getInstance() {
        if (instance == null)
            instance = new AppParams();
        return instance;
    }

    private boolean m_french;

    private AppParams() {
        m_french = true;
        m_fragment = false;
    }
/*
    public void removeFragment() {
        if(m_fragment) {
            Fragment fragment = m_FragmentManager.findFragmentById(R.id.IP_fragment);
            FragmentTransaction fragmentTransaction = m_FragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up);
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            m_fragment = false;
        }
    }
*/
    public boolean getM_french() {
        return m_french;
    }

    public void setM_french(boolean m_french) {
        this.m_french = m_french;
    }

    public boolean isM_fragment() {
        return m_fragment;
    }

    public void setM_fragment(boolean m_fragment) {
        this.m_fragment = m_fragment;
    }

    public FragmentManager getM_FragmentManager() {
        return m_FragmentManager;
    }

    public void setM_FragmentManager(FragmentManager m_FragmentManager) {
        this.m_FragmentManager = m_FragmentManager;
    }
}
