package com.ceri.visitechateau.files;

import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.NavigationView;

import com.ceri.visitechateau.main.MainActivity;
import com.ceri.visitechateau.R;
import com.ceri.visitechateau.entities.chateau.Location;
import com.ceri.visitechateau.entities.chateau.Visit;
import com.google.android.gms.drive.HomeActivity;

import java.io.File;

/**
 * Created by Maxime
 */
public class FileManager {
    final public static File SD_CARD = Environment.getExternalStorageDirectory();
    final public static String MEDIA_PATH = "/VisiteChateau";
    final public static String ZIP = ".zip";
    final public static String PHOTOS = "photo";
    final public static String INTERIEUR = "interieur";
    final public static String VIDEOS = "video";
    final public static String _360 = "360";
    final public static String OVERVIEW_FOLDER = "visite-overview";
    final public static String INFO_FOLDER = "visite-info";
    final public static String NAME_EN = "name_en.txt";
    final public static String PRESENTATION_FR = "content_fr.txt";
    final public static String PRESENTATION_EN = "content_en.txt";
    final public static String LENGTH_FR = "duree_fr.txt";
    final public static String LENGTH_EN = "duree_en.txt";
    final public static String MARKER = "marker.txt";
    private Location chateauWorkspace;

    private static FileManager INSTANCE = new FileManager();

    // Singleton
    public static FileManager getInstance() {
        return INSTANCE;
    }

    public FileManager() {
        Init();
    }

    /**
     * @return the chateauWorkspace
     */
    public Location getChateauWorkspace() {
        return chateauWorkspace;
    }

    /**
     * @param chateauWorkspace the chateauWorkspace to set
     */
    public void setChateauWorkspace(Location chateauWorkspace) {
        this.chateauWorkspace = chateauWorkspace;
    }

    // Init global media workspace
    public void Init() {
        this.setChateauWorkspace(new Location());
        InitChateau();
    }

    // Init chateau media workspace
    public void InitChateau() {
        FileTools.ListVisitChateau(SD_CARD + MEDIA_PATH, chateauWorkspace);
    }

    // Update media
    public static void UpdateMedia() {
        // By precaution, delete zip
        FileTools.Delete(SD_CARD + MEDIA_PATH + ZIP);
        Intent intent = new Intent(MainActivity.getContext(), HomeActivity.class);
        MainActivity.getContext().startActivity(intent);
    }

    // Create menu and list visits
    public static void ListVisits(NavigationView m_NavigationView, boolean french) {
        String name;
        int groupId = 0, itemId = 1, order = 0;
        if (FileManager.getInstance().getChateauWorkspace().getV() != null) {
            for (Visit v : FileManager.getInstance().getChateauWorkspace().getV()) {
                if(french)
                    name = v.getName();
                else
                    name = v.getNameEN();
                m_NavigationView.getMenu().add(groupId, itemId, order, name);
                itemId++;
                order++;
            }
        }
        if(!FileManager.getInstance().getChateauWorkspace().getV().isEmpty())
            order--;
        if (french) {
            m_NavigationView.getMenu().addSubMenu(groupId, itemId, order, R.string.option_section).add(R.string.action_section_1);
        } else {
            m_NavigationView.getMenu().addSubMenu(groupId, itemId, order, R.string.option_section).add(R.string.action_section_english_1);
        }
    }
}
