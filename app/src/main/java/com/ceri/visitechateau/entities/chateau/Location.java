package com.ceri.visitechateau.entities.chateau;

import java.util.ArrayList;

/**
 * @author Maxime
 *
 */
public class Location {
	
    private ArrayList<Visit> V;
    
    public Location() {
      this.setV(new ArrayList<Visit>());
    }

    /**
     * @return the V
     */
    public ArrayList<Visit> getV() {
      return V;
    }

    /**
     * @param V the V to set
     */
    public void setV(ArrayList<Visit> V) {
      this.V = V;
    }

    // Add a visit to a location
    public void addVisit(Visit v) {
        this.V.add(v);
    }

    // Search a visit object by name
    public Visit searchVisit(String name, boolean french) {
        String tmpName;
        for(Visit tmpV : getV()) {
            if(!french) {
                tmpName = tmpV.getNameEN();
            }
            else {
                tmpName = tmpV.getName();
            }
            if(tmpName.equals(name)) {
                return tmpV;
            }
        }
        return null;
    }
}
