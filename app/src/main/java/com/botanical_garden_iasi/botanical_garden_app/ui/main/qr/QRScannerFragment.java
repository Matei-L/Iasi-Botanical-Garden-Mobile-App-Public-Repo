package com.botanical_garden_iasi.botanical_garden_app.ui.main.qr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.helpers.permissions.CameraPermissionHelper;
import com.botanical_garden_iasi.botanical_garden_app.helpers.permissions.PermissionCallback;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import dagger.android.support.DaggerFragment;

public class QRScannerFragment extends DaggerFragment {
    private static final String TAG = "QRScannerFragment";

    private DecoratedBarcodeView dbvScanner;
    private CameraPermissionHelper cameraPermissionHelper;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qr_scanner_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        dbvScanner = view.findViewById(R.id.dbvBarcode);
        dbvScanner.setStatusText("");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Request camera access
        cameraPermissionHelper = new CameraPermissionHelper(this, new PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                startTheQRScanner();
            }

            @Override
            public void onPermissionDenied() {
                openPlantsListFragment();
            }
        });
        cameraPermissionHelper.request();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startTheQRScanner() {
        dbvScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                int id = 0;
                try {
                    id = Integer.parseInt(result.getText());
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                openPlantInfoActivity(id);
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        resumeScanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseScanner();
    }

    private void resumeScanner() {
        if (!dbvScanner.isActivated())
            dbvScanner.resume();
    }

    private void pauseScanner() {
        dbvScanner.pause();
    }

    private void openPlantInfoActivity(int plantID) {
        QRScannerFragmentDirections.ActionNavigationQrToNavigationPlantInfoActivity
                action = QRScannerFragmentDirections.actionNavigationQrToNavigationPlantInfoActivity();
        action.setPlantId(plantID);

        navController.navigate(action);
        pauseScanner();
    }

    private void openPlantsListFragment() {
        NavDirections direction = QRScannerFragmentDirections
                .actionNavigationQrToNavigationPlantsList();

        navController.navigate(direction);
        pauseScanner();
    }

}
