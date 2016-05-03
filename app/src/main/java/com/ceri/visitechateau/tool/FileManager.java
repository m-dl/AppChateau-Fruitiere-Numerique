package com.ceri.visitechateau.tool;

import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.NavigationView;

import com.ceri.visitechateau.MainActivity;
import com.ceri.visitechateau.R;
import com.google.android.gms.drive.HomeActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maxime
 */
public class FileManager {

    public static final Pattern GPS = Pattern.compile(
            "([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)",
            Pattern.CASE_INSENSITIVE);

    public static File SD_CARD = Environment.getExternalStorageDirectory();
    public static String MEDIA_PATH = "/VisiteTablette";
    public static String ZIP = ".zip";

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

    // Check if folder or file exists
    public static boolean Exist(File f) {
        if (f.exists())
            return true;
        return false;
    }

    // Update media
    public static void UpdateMedia() {
        // By precaution, delete zip
        Delete(SD_CARD + MEDIA_PATH + ZIP);
        Intent intent = new Intent(MainActivity.getContext(), HomeActivity.class);
        MainActivity.getContext().startActivity(intent);
    }

    // Create menu and list visits
    public static void ListVisits(NavigationView m_NavigationView, boolean french) {
        File folder = new File(SD_CARD + MEDIA_PATH);
        boolean emptyVisitList = true;
        int groupId = 0, itemId = 1, order = 0;
        if (Exist(folder)) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        emptyVisitList = false;
                        m_NavigationView.getMenu().add(groupId, itemId, order, f.getName());
                        itemId++;
                        order++;
                    }
                }
            }
        }
        if (!emptyVisitList)
            order--;
        if (french) {
            m_NavigationView.getMenu().addSubMenu(groupId, itemId, order, R.string.option_section).add(R.string.action_section_1);
        } else {
            m_NavigationView.getMenu().addSubMenu(groupId, itemId, order, R.string.option_section).add(R.string.action_section_english_1);
        }
    }

    // Parse GPS coordinates
    public static boolean ParseCoordinates(String input) {
        Matcher matcher = GPS.matcher(input);
        boolean isMatch = matcher.matches();

        if(!isMatch) {
            return false;
        }
        return true;
    }

}
