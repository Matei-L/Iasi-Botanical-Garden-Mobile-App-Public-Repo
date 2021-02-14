package com.botanical_garden_iasi.botanical_garden_app.helpers.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class GPSChecker {

    private static final String TAG = "GPSChecker";
    private final static int REQUEST_CODE = 12;

    private PermissionCallback permissionCallback;
    private Activity activity;
    private Context context;

    public GPSChecker(Activity activity, PermissionCallback permissionCallback) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.permissionCallback = permissionCallback;
    }

    public GPSChecker(Fragment fragment, PermissionCallback permissionCallback) {
        this.activity = fragment.getActivity();
        this.context = fragment.getContext();
        this.permissionCallback = permissionCallback;
    }


    public void asyncCheck() {
        if (!isGpsEnabled()) {
            buildAlertMessageNoGps();
        } else {
            permissionCallback.onPermissionGranted();
        }
    }

    private boolean isGpsEnabled() {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        return manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(enableGpsIntent, REQUEST_CODE);
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (isGpsEnabled()) {
                permissionCallback.onPermissionGranted();
            } else {
                permissionCallback.onPermissionDenied();
            }
        }
    }

}

