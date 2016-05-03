package com.google.android.gms.drive.tools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Maxime
 */
public class FileManager {

    // Create a directory
    public static void CreateDirectory(File dir) {
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

    // Delete a file / directory
    public static void Delete(String path) {
        File file = new File(path);
        if(Exist(file)) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Check if folder or file exists
    public static boolean Exist(File f) {
        if(f.exists())
            return true;
        return false;
    }

    // Delete folder content
    public static void EmptyFolderContent(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    EmptyFolderContent(f);
                    folder.delete();
                } else {
                    f.delete();
                }
            }
        }
    }

    // Compare numbers
    public static boolean CompareNumbers(long a, long b) {
        return a < b;
    }
}
