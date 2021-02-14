package com.botanical_garden_iasi.botanical_garden_app.helpers.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

abstract class PermissionHelper {
    String TAG = "PermissionHelper";
    int REQUEST_CODE = 1;
    String[] permissions = new String[]{};

    private PermissionCallback permissionCallback;
    private Activity activity = null;
    private Fragment fragment = null;
    private Context context;

    PermissionHelper(Activity activity, PermissionCallback permissionCallback) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.permissionCallback = permissionCallback;
    }

    PermissionHelper(Fragment fragment, PermissionCallback permissionCallback) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.permissionCallback = permissionCallback;
    }

    private boolean checkPermission(Context context) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public void request() {
        if (checkPermission(context)) {
            Log.i(TAG, "Fired a permission request.");
            if (activity != null) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
            } else {
                fragment.requestPermissions(permissions, REQUEST_CODE);
            }
        } else {
            Log.i(TAG, "Permission already granted.");
            permissionCallback.onPermissionGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            Log.i(TAG, "Request permission's result caught.");
            boolean denied = false;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    denied = true;
                }
            }
            if (!denied) {
                Log.i(TAG, "Permission granted. Calling onPermissionGranted.");
                permissionCallback.onPermissionGranted();
            } else {
                Log.i(TAG, "Permission denied. Calling onPermissionDenied.");
                permissionCallback.onPermissionDenied();
            }
        }
    }
}
