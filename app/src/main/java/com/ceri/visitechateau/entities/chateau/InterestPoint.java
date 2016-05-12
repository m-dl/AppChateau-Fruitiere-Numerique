package com.ceri.visitechateau.entities.chateau;

import com.ceri.visitechateau.files.FileManager;
import com.ceri.visitechateau.files.FileTools;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Maxime
 */
public class InterestPoint implements Serializable {
	private File presentation_FR, presentation_EN, marker, picture, name_EN;
	private ArrayList<File> photos, interieur, _360, videos;
	private String name, nameEN;
	private int floor;
	private double coordX, coordY;
	
	/**
	 * @param pathFrom
	 */
	public InterestPoint(String pathFrom, String name) {
		if(!FileTools.Exist(new File(pathFrom)))
            FileTools.CreateDirectory(pathFrom);
		
		this.presentation_FR = new File(pathFrom + "/" + FileManager.PRESENTATION_FR);
		this.presentation_EN = new File(pathFrom + "/" + FileManager.PRESENTATION_EN);
		this.name_EN = new File(pathFrom + "/" + FileManager.NAME_EN);
		this.marker = new File(pathFrom + "/" + FileManager.MARKER);

		initInterestPoint(pathFrom);

		this.photos = FileTools.ListFolderPictures(pathFrom + "/" + FileManager.PHOTOS);
		this._360 = FileTools.ListFolderPictures(pathFrom + "/" + FileManager._360);
		this.interieur = FileTools.ListFolderPictures(pathFrom + "/" + FileManager.INTERIEUR);
		this.videos = FileTools.ListFolderVideos(pathFrom + "/" + FileManager.VIDEOS);
		ArrayList<File> tmpPicture = FileTools.ListFolderPictures(pathFrom);
		if(!tmpPicture.isEmpty())
			this.picture = tmpPicture.get(0);
		this.name = name;
		this.nameEN = readName_EN();
		this.coordX = 0;
		this.coordY = 0;
		this.floor = 0;
		readMarker();
	}
	
	private void initInterestPoint(String pathFrom) {
		if(!FileTools.Exist(this.presentation_FR))
			FileTools.CreateFile(pathFrom + "/" + FileManager.PRESENTATION_FR);
		if(!FileTools.Exist(this.presentation_EN))
			FileTools.CreateFile(pathFrom + "/" + FileManager.PRESENTATION_EN);
		if(!FileTools.Exist(this.name_EN))
			FileTools.CreateFile(pathFrom + "/" + FileManager.NAME_EN);
		if(!FileTools.Exist(this.marker))
			FileTools.CreateFile(pathFrom + "/" + FileManager.MARKER);
	}
	
	public String readPresentation_FR() {
		return FileTools.Read(this.presentation_FR);
	}

	public String readPresentation_EN() {
		return FileTools.Read(this.presentation_EN);
	}

	public String readName_EN() {
		return FileTools.Read(this.name_EN);
	}
	
	public void readMarker() {
		FileTools.ParseCoordinates(this);
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

	public File getMarker() {
		return marker;
	}

	public void setMarker(File marker) {
		this.marker = marker;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<File> getInterieur() {
		return interieur;
	}

	public void setInterieur(ArrayList<File> interieur) {
		this.interieur = interieur;
	}

	public ArrayList<File> getVideos() {
		return videos;
	}

	public void setVideos(ArrayList<File> videos) {
		this.videos = videos;
	}

	public ArrayList<File> get_360() {
		return _360;
	}

	public void set_360(ArrayList<File> _360) {
		this._360 = _360;
	}

	public File getName_EN() {
		return name_EN;
	}

	public void setName_EN(File name_EN) {
		this.name_EN = name_EN;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}
}
