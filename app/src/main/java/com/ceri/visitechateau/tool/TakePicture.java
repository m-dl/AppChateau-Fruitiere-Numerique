package com.ceri.visitechateau.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TakePicture {

    private Context m_Context;
    private Activity m_Activity;
    final public static String PHOTO_FOLDER = "/VisiteChateau";
    final public static File PICTURE_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + PHOTO_FOLDER);

    public TakePicture(Activity activity) {
        m_Activity = activity;
        m_Context = m_Activity.getApplicationContext();
    }

    public void photo() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(m_Context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //todo
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                m_Activity.startActivityForResult(takePictureIntent, 1337);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + PHOTO_FOLDER);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    // empty visitors pictures folder
    public static void updateVisitorPhoto() {
        com.google.android.gms.drive.tools.FileManager.CreateDirectory(PICTURE_FOLDER);
        com.google.android.gms.drive.tools.FileManager.EmptyFolderContent(PICTURE_FOLDER);
    }

}
