/**
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.drive;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.sample.demo.R;
import com.google.android.gms.drive.tools.FileManager;
import com.google.android.gms.drive.tools.ZipManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Maxime
 * Query file with given title and download it
 */
public class HomeActivity extends BaseActivity {

    private static final String CHATEAU_MEDIAS = "VisiteChateau";
    private static final String ZIP_EXT = ".zip";
    private static final String DIR_FOR_DOWNLOADS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    private ProgressBar mProgressBar;
    private TextView mTextView;
    private DriveId mFileId;
    private int bytesExpected = 1;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main_drive);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarDl);
        mTextView = (TextView) findViewById(R.id.textView);
        mProgressBar.setMax(100);
    }

    // if connected to drive account, start the tasks
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        bytesExpected = 1;
        mProgressBar.setProgress(0);
        mTextView.setText(mTextView.getText() + "\n" + "La mise à jour des médias va débuter.");
        Query query = new Query.Builder()
                .addFilter(Filters.contains(SearchableField.TITLE, CHATEAU_MEDIAS))
                .build();
        Drive.DriveApi.query(getGoogleApiClient(), query)
                .setResultCallback(metadataCallback);
    }

    // callback result
    final private ResultCallback<DriveApi.MetadataBufferResult> metadataCallback =
            new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        mTextView.setText(mTextView.getText() + "\n" + "Erreur lors de la récupération du résultat.");
                        return;
                    }
                    if(result.getMetadataBuffer().getCount() != 0) {
                        bytesExpected = (int) result.getMetadataBuffer().get(0).getFileSize();
                        mFileId = result.getMetadataBuffer().get(0).getDriveId();
                        DriveFile file = mFileId.asDriveFile();
                        File tmpDevice = new File(DIR_FOR_DOWNLOADS + CHATEAU_MEDIAS);
                        long deviceDate = 0;
                        long driveDate = result.getMetadataBuffer().get(0).getCreatedDate().getTime();
                        if (FileManager.Exist(tmpDevice))
                            deviceDate = tmpDevice.lastModified();
                        result.release();
                        if (FileManager.CompareNumbers(deviceDate, driveDate)) {
                            mTextView.setText(mTextView.getText() + "\n" + "Téléchargement et mise à jour sur la tablette en cours ...");
                            new UpdateMediaAsyncTask(HomeActivity.this).execute(file);
                        } else {
                            showMessage("Les médias sont déjà à jour !");
                            finish();
                            return;
                        }
                    } else {
                        result.release();
                        showMessage("Aucun média trouvé, veuillez réessayer plus tard, ou mettre le Drive à jour !");
                        finish();
                        return;
                    }
                }
            };

    // Update the medias, async task
    public class UpdateMediaAsyncTask extends ApiClientAsyncTask<DriveFile, Void, Boolean> {

        public UpdateMediaAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected Boolean doInBackgroundConnected(DriveFile... args) {
            DriveFile file = args[0];

            DriveApi.DriveContentsResult driveContentsResult = file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return false;
            }

            DriveContents driveContents = driveContentsResult.getDriveContents();
            InputStream in = driveContents.getInputStream();

            try {
                File fileToDownload = new File(DIR_FOR_DOWNLOADS + CHATEAU_MEDIAS + ZIP_EXT);
                OutputStream out = new FileOutputStream(fileToDownload);
                byte[] buf = new byte[1024];
                int len;
                long bytesDownloaded = 1;
                while((len=in.read(buf))>0){
                    // Update progress dialog with the latest progress.
                    bytesDownloaded += len;
                    long progress = bytesDownloaded*100/bytesExpected;
                    mProgressBar.setProgress((int)progress);
                    // Write the file
                    out.write(buf,0,len);
                }
                out.close();
                in.close();

                // Delete folder, unzip new zip, delete zip
                if(FileManager.Exist(fileToDownload)) {
                    FileManager.Delete(DIR_FOR_DOWNLOADS + CHATEAU_MEDIAS);
                    ZipManager.unZip(DIR_FOR_DOWNLOADS + CHATEAU_MEDIAS + ZIP_EXT, DIR_FOR_DOWNLOADS);
                    FileManager.Delete(DIR_FOR_DOWNLOADS + CHATEAU_MEDIAS + ZIP_EXT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result) {
                showMessage("Erreur lors du téléchargement - les médias n'ont pas été mis à jour.");
                finish();
                return;
            }
            showMessage("Mise jour des médias réussie !");
            finish();
        }
    }
}
