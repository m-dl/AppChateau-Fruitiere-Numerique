package com.ceri.visitechateau;

/**
 * Created by Maxime
 */
public class AppParams {
    public static AppParams instance;

    public static AppParams getInstance() {
        if (instance == null)
            instance = new AppParams();
        return instance;
    }

    private boolean m_french;

    private AppParams() {
        m_french = true;
    }

    public boolean getM_french() {
        return m_french;
    }

    public void setM_french(boolean m_french) {
        this.m_french = m_french;
    }
}
