package com.botanical_garden_iasi.botanical_garden_app.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MBTilesInternalStorageProxi {
    private static final String TAG = "MBTilesInternalStorageProxi";
    Context context;

    public MBTilesInternalStorageProxi(Context context) {
        this.context = context;
    }

    private String getInternalStoragePath(String relativePath) {
        return context.getFilesDir() + "/" + relativePath;
    }

    private void copyToInternalStorage(String relativePath) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(relativePath);
        OutputStream outputStream = new FileOutputStream(getInternalStoragePath(relativePath));

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public File getProxi(String relativePath) {
        File proxiPath = new File(getInternalStoragePath(relativePath));
        if (!proxiPath.exists()) {
            try {
                copyToInternalStorage(relativePath);
            } catch (IOException e) {
                Log.e(TAG, "getProxi: Error wile copying the file.", e);
            }
        }
        return proxiPath;
    }
}
