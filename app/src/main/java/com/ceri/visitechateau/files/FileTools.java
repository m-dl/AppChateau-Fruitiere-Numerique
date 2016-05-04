package com.ceri.visitechateau.files;

import com.ceri.visitechateau.entities.chateau.InterestPoint;
import com.ceri.visitechateau.entities.chateau.Location;
import com.ceri.visitechateau.entities.chateau.Visit;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Maxime
 */
public class FileTools {
	public static final Pattern GPS = Pattern.compile(
			"([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)", 
	        Pattern.CASE_INSENSITIVE);
	
    public static final String[] IMAGES_EXTENSIONS = new String[]{
        "gif", "png", "bmp", "jpeg", "jpg", "GIF", "PNG", "BMP", "JPEG", "JPG"
    };

    public static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : IMAGES_EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    
    public static final String[] VIDEOS_EXTENSIONS = new String[]{
        "mp4", "avi", "mkv", "webm", "mov", "MP4", "AVI", "MKV", "WEBM", "MOV"
    };

    public static final FilenameFilter VIDEO_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : VIDEOS_EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

	// Create a file
	public static void CreateFile(String path) {
		try {
		      File file = new File(path);
		      if(!file.createNewFile()){ }
	    	} catch(IOException e) {
		      e.printStackTrace();
		}
	}
	
	// Create a directory
	public static void CreateDirectory(String path) {
		File dir = new File(path);
		// if the directory does not exist, create it
		if(Exist(dir)) { }
		else {
			try {
		        dir.mkdir();
		    } 
		    catch(SecurityException se){
		    	 se.printStackTrace();
		    }    
		}
	}
	
	// Read from a file
	public static String Read(File file) {
		String output = null;
	    try {
	        FileReader reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        output = new String(chars);
	        reader.close();
	    } catch(IOException e) {
	        e.printStackTrace();
	    }
		return output;
	}

	// Check if folder or file exists
	public static boolean Exist(File f) {
		if(f.exists())
			return true;
		return false;
	}

	// Delete a file / directory
	public static void Delete(String path) {
		File file = new File(path);
		if (Exist(file)) {
			try {
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    // Clear string all spaces / line jump
    public static String StringClearAllSpaces(String input) {
    	return input.replaceAll("\\s+",""); // removes spaces / line jump
    }
    
	// List all pictures from a folder
	public static ArrayList<File> ListFolderPictures(String p) {
		ArrayList<File> picturesList = new ArrayList<File>();
		File pathFrom = new File(p);
		if(!Exist(pathFrom)) 
			return picturesList;
		File[] list = pathFrom.listFiles(IMAGE_FILTER);
		for(File f : list) {
        	// add picture to arraylist
			picturesList.add(f);
        }
		return picturesList;
	}

	// List all videos from a folder
	public static ArrayList<File> ListFolderVideos(String p) {
		ArrayList<File> videosList = new ArrayList<File>();
		File pathFrom = new File(p);
		if(!Exist(pathFrom)) 
			return videosList;
		File[] list = pathFrom.listFiles(VIDEO_FILTER);
		for(File f : list) {
        	// add video to arraylist
			videosList.add(f);
        }
		return videosList;
	}

	// List all Chateau visits from a location
	public static void ListVisitChateau(String pathFrom, Location l) {
		File fileFrom = new File(pathFrom);
        File[] list = fileFrom.listFiles();
        if(!Exist(fileFrom)) 
        	return;
        for(File file : list){
            if(file.isDirectory()){ // Visits' folder
            	ListVisitContentChateau(pathFrom + "/" + file.getName(), file.getName(), l);
            }
        }
	}
	
	// List a Chateau visit folder content
    public static void ListVisitContentChateau(String pathFrom, String visitName, Location l) {
        File fileFrom = new File(pathFrom);
        File[] list = fileFrom.listFiles();
        
        if(!Exist(fileFrom)) 
        	return;
        
        Visit tmpVisit = new Visit(pathFrom, visitName);
        for(File file : list) {
            if(file.isDirectory()){ // Overview or Info or IP folder
                if(!file.getName().equals(FileManager.OVERVIEW_FOLDER) && !file.getName().equals(FileManager.INFO_FOLDER)) {
                	InterestPoint tmpIP = new InterestPoint(pathFrom + "/" + file.getName(), file.getName());
                	tmpVisit.addInterestPoint(tmpIP);
                }
            }
        }
        l.addVisit(tmpVisit);
    }
    
    // Parse GPS coordinates
    public static String ParseCoordinates(String input) {
    	input = StringClearAllSpaces(input);
    	Matcher matcher = GPS.matcher(input);
    	boolean isMatch = matcher.matches();
    
    	if(!isMatch) {
    		input = "";
    		System.out.println("Coordonnées saisies invalides !");
    	}
        return input;
    }

}