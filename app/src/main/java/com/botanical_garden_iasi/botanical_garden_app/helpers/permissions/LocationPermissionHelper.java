package com.botanical_garden_iasi.botanical_garden_app.helpers.permissions;

import android.Manifest;
import android.app.Activity;

import androidx.fragment.app.Fragment;

public class LocationPermissionHelper extends PermissionHelper {

    public LocationPermissionHelper(Activity activity, PermissionCallback permissionCallback) {
        super(activity, permissionCallback);
        init();
    }

    public LocationPermissionHelper(Fragment fragment, PermissionCallback permissionCallback) {
        super(fragment, permissionCallback);
        init();
    }

    private void init() {
        TAG = "LocationPermissionHelper";
        REQUEST_CODE = 11;
        permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    }
}