package com.botanical_garden_iasi.botanical_garden_app.helpers.permissions;

public interface PermissionCallback {
    void onPermissionGranted();
    void onPermissionDenied();
}
