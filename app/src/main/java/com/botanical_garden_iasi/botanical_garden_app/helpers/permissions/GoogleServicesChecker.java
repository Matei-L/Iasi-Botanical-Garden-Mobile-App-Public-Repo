package com.botanical_garden_iasi.botanical_garden_app.helpers.permissions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GoogleServicesChecker {

    private static final String TAG = "GoogleServicesChecker";
    private final static int REQUEST_CODE = 13;

    private Activity activity = null;
    private Context context;

    public GoogleServicesChecker(Activity activity) {
        this.activity = activity;
        this.context = activity.getBaseContext();
    }

    public GoogleServicesChecker(Fragment fragment) {
        this.activity = fragment.getActivity();
        this.context = fragment.getContext();
    }

    public boolean check() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an solvable error occurred
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, REQUEST_CODE);
            dialog.show();
        } else {
            Toast.makeText(context, "Google services are missing.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
