package com.botanical_garden_iasi.botanical_garden_app.helpers.permissions;

import android.Manifest;
import android.app.Activity;

import androidx.fragment.app.Fragment;

public class StoragePermissionHelper extends PermissionHelper {

    public StoragePermissionHelper(Activity activity, PermissionCallback permissionCallback) {
        super(activity, permissionCallback);
        init();
    }

    public StoragePermissionHelper(Fragment fragment, PermissionCallback permissionCallback) {
        super(fragment, permissionCallback);
        init();
    }

    private void init() {
        TAG = "StoragePermissionHelper";
        REQUEST_CODE = 14;
        permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }
}