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
public class Info implements Serializable {
	private File content_FR, content_EN, picture;
	private ArrayList<File> photos;
	
	/**
	 * @param pathFrom
	 */
	public Info(String pathFrom) {
		if(!FileTools.Exist(new File(pathFrom)))
            FileTools.CreateDirectory(pathFrom);
		
		this.content_FR = new File(pathFrom + "/" + FileManager.PRESENTATION_FR);
		this.content_EN = new File(pathFrom + "/" + FileManager.PRESENTATION_EN);
		
		initInfo(pathFrom);

		ArrayList<File> tmpPicture = FileTools.ListFolderPictures(pathFrom);
		if(!tmpPicture.isEmpty())
			this.picture = tmpPicture.get(0);
		this.photos = FileTools.ListFolderPictures(pathFrom + "/" + FileManager.PHOTOS);
	}

	// check and create files if not exist
	private void initInfo(String pathFrom) {
		if(!FileTools.Exist(this.content_FR))
			FileTools.CreateFile(pathFrom + "/" + FileManager.PRESENTATION_FR);
		if(!FileTools.Exist(this.content_EN))
			FileTools.CreateFile(pathFrom + "/" + FileManager.PRESENTATION_EN);
	}

	// read content fr
	public String readContent_FR() {
		return FileTools.Read(this.content_FR);
	}

	// read content en
	public String readContent_EN() {
		return FileTools.Read(this.content_EN);
	}

	
	public File getContent_FR() {
		return content_FR;
	}
	public void setContent_FR(File content_FR) {
		this.content_FR = content_FR;
	}
	public File getContent_EN() {
		return content_EN;
	}
	public void setContent_EN(File content_EN) {
		this.content_EN = content_EN;
	}
	public File getPicture() {
		return picture;
	}
	public void setPicture(File picture) {
		this.picture = picture;
	}
	public ArrayList<File> getPhotos() {
		return photos;
	}
	public void setPhotos(ArrayList<File> photos) {
		this.photos = photos;
	}

}
