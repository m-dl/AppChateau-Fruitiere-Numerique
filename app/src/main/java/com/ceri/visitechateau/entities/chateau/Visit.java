package com.ceri.visitechateau.entities.chateau;

import com.ceri.visitechateau.files.FileManager;
import com.ceri.visitechateau.files.FileTools;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Maxime
 */
public class Visit {

    private ArrayList<InterestPoint> IP;
    private Overview overview;
    private Info info;
    private File name_EN;
    private String name, nameEN;

    public Visit(String pathFrom, String name) {
    	if(!FileTools.Exist(new File(pathFrom)))
            FileTools.CreateDirectory(pathFrom);
		this.name_EN = new File(pathFrom + "/" + FileManager.NAME_EN);
    	
        initVisit(pathFrom);

        this.setIP(new ArrayList<InterestPoint>());
        this.setOverview(new Overview(pathFrom + "/" + FileManager.OVERVIEW_FOLDER));
        this.setInfo(new Info(pathFrom + "/" + FileManager.INFO_FOLDER));
        this.name = name;
        this.nameEN = readName_EN();
    }

    private void initVisit(String pathFrom) {
        if (!FileTools.Exist(new File(pathFrom + "/" + FileManager.OVERVIEW_FOLDER)))
            FileTools.CreateDirectory(pathFrom + "/" + FileManager.OVERVIEW_FOLDER);
        if (!FileTools.Exist(new File(pathFrom + "/" + FileManager.INFO_FOLDER)))
            FileTools.CreateDirectory(pathFrom + "/" + FileManager.INFO_FOLDER);
		if(!FileTools.Exist(this.name_EN))
			FileTools.CreateFile(pathFrom + "/" + FileManager.NAME_EN);
    }

    /**
     * @return the iP
     */
    public ArrayList<InterestPoint> getIP() {
        return IP;
    }

    /**
     * @param IP the iP to set
     */
    public void setIP(ArrayList<InterestPoint> IP) {
        this.IP = IP;
    }

    public Overview getOverview() {
        return overview;
    }

    public void setOverview(Overview overview) {
        this.overview = overview;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public File getName_EN() {
        return name_EN;
    }

    public void setName_EN(File name_EN) {
        this.name_EN = name_EN;
    }

    public String readName_EN() {
		return FileTools.Read(this.name_EN);
	}

    // Add an interest point to a visit
    public void addInterestPoint(InterestPoint IP) {
        this.IP.add(IP);
    }
	
}
