package com.botanical_garden_iasi.botanical_garden_app.helpers.permissions;

import android.Manifest;
import android.app.Activity;

import androidx.fragment.app.Fragment;

public class CameraPermissionHelper extends PermissionHelper {

    public CameraPermissionHelper(Activity activity, PermissionCallback permissionCallback) {
        super(activity, permissionCallback);
        init();
    }

    public CameraPermissionHelper(Fragment fragment, PermissionCallback permissionCallback) {
        super(fragment, permissionCallback);
        init();
    }

    private void init() {
        TAG = "CameraPermissionHelper";
        REQUEST_CODE = 10;
        permissions = new String[]{Manifest.permission.CAMERA};
    }
}