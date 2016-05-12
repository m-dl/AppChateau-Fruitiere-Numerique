package com.ceri.visitechateau.params;

import com.ceri.visitechateau.entities.chateau.Location;
import com.ceri.visitechateau.entities.chateau.Visit;

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
    private int currentFloor;
    private Visit currentVisit;

    private AppParams() {
        m_french = true;
        currentFloor = Location.FLOOR_ONE;
        currentVisit = null;
    }
    public boolean getM_french() {
        return m_french;
    }

    public void setM_french(boolean m_french) {
        this.m_french = m_french;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Visit getCurrentVisit() {
        return currentVisit;
    }

    public void setCurrentVisit(Visit currentVisit) {
        this.currentVisit = currentVisit;
    }
}
