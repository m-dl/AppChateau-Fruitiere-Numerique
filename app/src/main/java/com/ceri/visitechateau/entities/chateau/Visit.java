package com.ceri.visitechateau.entities.chateau;

import com.ceri.visitechateau.files.FileManager;
import com.ceri.visitechateau.files.FileTools;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Maxime
 */
public class Visit implements Serializable {

    // one array of IP by floor
    private ArrayList<InterestPoint> IP1, IP2, IP3;
    private Overview overview;
    private Info info;
    private File name_EN;
    private String name, nameEN;

    public Visit(String pathFrom, String name) {
    	if(!FileTools.Exist(new File(pathFrom)))
            FileTools.CreateDirectory(pathFrom);
		this.name_EN = new File(pathFrom + "/" + FileManager.NAME_EN);
    	
        initVisit(pathFrom);

        this.setIP1(new ArrayList<InterestPoint>());
        this.setIP2(new ArrayList<InterestPoint>());
        this.setIP3(new ArrayList<InterestPoint>());
        this.setOverview(new Overview(pathFrom + "/" + FileManager.OVERVIEW_FOLDER));
        this.setInfo(new Info(pathFrom + "/" + FileManager.INFO_FOLDER));
        this.name = name;
        this.nameEN = readName_EN();
    }

    // check and create files if not exist
    private void initVisit(String pathFrom) {
        if (!FileTools.Exist(new File(pathFrom + "/" + FileManager.OVERVIEW_FOLDER)))
            FileTools.CreateDirectory(pathFrom + "/" + FileManager.OVERVIEW_FOLDER);
        if (!FileTools.Exist(new File(pathFrom + "/" + FileManager.INFO_FOLDER)))
            FileTools.CreateDirectory(pathFrom + "/" + FileManager.INFO_FOLDER);
		if(!FileTools.Exist(this.name_EN))
			FileTools.CreateFile(pathFrom + "/" + FileManager.NAME_EN);
    }

    public ArrayList<InterestPoint> getIP1() {
        return IP1;
    }

    public void setIP1(ArrayList<InterestPoint> IP1) {
        this.IP1 = IP1;
    }

    public ArrayList<InterestPoint> getIP2() {
        return IP2;
    }

    public void setIP2(ArrayList<InterestPoint> IP2) {
        this.IP2 = IP2;
    }

    public ArrayList<InterestPoint> getIP3() {
        return IP3;
    }

    public void setIP3(ArrayList<InterestPoint> IP3) {
        this.IP3 = IP3;
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
    public void addInterestPoint(InterestPoint IP, ArrayList<InterestPoint> IPArray) {
        IPArray.add(IP);
    }
	
}
