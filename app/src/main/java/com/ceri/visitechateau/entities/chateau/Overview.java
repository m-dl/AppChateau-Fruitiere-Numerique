package com.ceri.visitechateau.entities.chateau;

import com.ceri.visitechateau.files.FileManager;
import com.ceri.visitechateau.files.FileTools;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Maxime
 *
 */
public class Overview implements Serializable {
	private File presentation_FR, presentation_EN, length_FR, length_EN;
	private ArrayList<File> imagesContent;
	
	/**
	 * @param pathFrom
	 */
	public Overview(String pathFrom) {
		if(!FileTools.Exist(new File(pathFrom)))
            FileTools.CreateDirectory(pathFrom);
		
		this.presentation_FR = new File(pathFrom + "/" + FileManager.PRESENTATION_FR);
		this.presentation_EN = new File(pathFrom + "/" + FileManager.PRESENTATION_EN);
		this.length_FR = new File(pathFrom + "/" + FileManager.LENGTH_FR);
		this.length_EN = new File(pathFrom + "/" + FileManager.LENGTH_EN);
		
		initOverview(pathFrom);
		
		this.imagesContent = FileTools.ListFolderPictures(pathFrom);
	}

	// check and create files if not exist
	private void initOverview(String pathFrom) {
		if(!FileTools.Exist(this.presentation_FR))
			FileTools.CreateFile(pathFrom + "/" + FileManager.PRESENTATION_FR);
		if(!FileTools.Exist(this.presentation_EN))
			FileTools.CreateFile(pathFrom + "/" + FileManager.PRESENTATION_EN);
		if(!FileTools.Exist(this.length_FR))
			FileTools.CreateFile(pathFrom + "/" + FileManager.LENGTH_FR);
		if(!FileTools.Exist(this.length_EN))
			FileTools.CreateFile(pathFrom + "/" + FileManager.LENGTH_EN);
	}

	// read presentation fr
	public String readPresentation_FR() {
		return FileTools.Read(this.presentation_FR);
	}

	// read presentation en
	public String readPresentation_EN() {
		return FileTools.Read(this.presentation_EN);
	}

	// read length fr
	public String readLength_FR() {
		return FileTools.Read(this.length_FR);
	}

	// read length en
	public String readLength_EN() {
		return FileTools.Read(this.length_EN);
	}

	public File getPresentation_FR() {
		return presentation_FR;
	}

	public void setPresentation_FR(File presentation_FR) {
		this.presentation_FR = presentation_FR;
	}

	public File getPresentation_EN() {
		return presentation_EN;
	}

	public void setPresentation_EN(File presentation_EN) {
		this.presentation_EN = presentation_EN;
	}

	public File getLength_FR() {
		return length_FR;
	}

	public void setLength_FR(File length_FR) {
		this.length_FR = length_FR;
	}

	public File getLength_EN() {
		return length_EN;
	}

	public void setLength_EN(File length_EN) {
		this.length_EN = length_EN;
	}

	public ArrayList<File> getImagesContent() {
		return imagesContent;
	}

}
