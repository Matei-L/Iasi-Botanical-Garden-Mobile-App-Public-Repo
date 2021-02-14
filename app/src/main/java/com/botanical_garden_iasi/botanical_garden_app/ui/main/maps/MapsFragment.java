package com.botanical_garden_iasi.botanical_garden_app.ui.main.maps;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;
import com.botanical_garden_iasi.botanical_garden_app.helpers.MBTilesInternalStorageProxi;
import com.botanical_garden_iasi.botanical_garden_app.helpers.permissions.GPSChecker;
import com.botanical_garden_iasi.botanical_garden_app.helpers.permissions.GoogleServicesChecker;
import com.botanical_garden_iasi.botanical_garden_app.helpers.permissions.LocationPermissionHelper;
import com.botanical_garden_iasi.botanical_garden_app.helpers.permissions.PermissionCallback;
import com.botanical_garden_iasi.botanical_garden_app.helpers.permissions.StoragePermissionHelper;
import com.botanical_garden_iasi.botanical_garden_app.maputils.src.tiles.ExpandedMBTilesTileProvider;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.section_info_activity.SectionInfoActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.others.FloatingActionButtonSwitch;
import com.botanical_garden_iasi.botanical_garden_app.viewmodels.ViewModelProviderFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.material.card.MaterialCardView;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MapsFragment extends DaggerFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = "MapsFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private MapsViewModel mapsViewModel;
    private StoragePermissionHelper storagePermissionHelper = null;
    private LocationPermissionHelper locationPermissionHelper;
    private GPSChecker gpsChecker;
    private GoogleServicesChecker googleServicesChecker;
    private NavController navController;
    private FloatingActionButtonSwitch floatingActionButtonSwitchOverlay;
    private FloatingActionButtonSwitch floatingActionButtonSwitchDetails;

    private MapView mapView;
    private GoogleMap googleMap;
    private Marker currentMarker = null;

    private SectionsListSelectorAdapter sectionsListSelectorAdapter;
    private RecyclerView sectionsPillsSelectorRecyclerView;

    private String[] tileOverlaysRelativePaths = {
            "plan_botanica.mbtiles",
            "plan_botanica_detaliat.mbtiles"};
    private List<TileOverlay> tileOverlays = null;
    private int argsSectionId = -1;

    private GeoApiContext geoApiContext = null;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location currentUserLocation = null;
    private Polyline currentPolyline = null;
    private List<LatLng> currentPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapsViewModel = new ViewModelProvider(this, providerFactory)
                .get(MapsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.maps_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLocationListeners();
        setupPermissionHelpers();
        redirectBasedOnBundleArguments();
    }

    private void initLocationListeners() {
        if (getActivity() != null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30*1000);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        currentUserLocation = location;
                        if (currentMarker != null) {
                            calculateDirections(currentMarker);
                        }
                    }
                }
            };
        }
    }

    private void redirectBasedOnBundleArguments() {
        if (getActivity() != null) {
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null) {
                int sectionId = bundle.getInt("sectionId");
                getActivity().getIntent().removeExtra("sectionId");
                if (sectionId > 0) {
                    this.argsSectionId = sectionId;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (storagePermissionHelper != null) {
            storagePermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        mapView = view.findViewById(R.id.mapView);

        floatingActionButtonSwitchOverlay = view.findViewById(R.id.floatingActionShowOverlay);
        floatingActionButtonSwitchDetails = view.findViewById(R.id.floatingActionShowDetails);
        sectionsPillsSelectorRecyclerView = view.findViewById(R.id.sections_recycler_view);

        setupSectionSelector();

        setupMapView(savedInstanceState);
        setupFloatingActionButtons();

        subscribeToViewModel();
    }

    private void setupSectionSelector() {
        sectionsListSelectorAdapter = new SectionsListSelectorAdapter();
        sectionsPillsSelectorRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        sectionsPillsSelectorRecyclerView.setAdapter(sectionsListSelectorAdapter);
        sectionsListSelectorAdapter.setOnSelectListener(selectedPosition -> {
            selectSection();
        });
    }

    private void selectSection() {
        Section selectedSection = sectionsListSelectorAdapter.getSelectedItem();
        if (selectedSection != null) {
            MarkerOptions markerOptions = new MarkerOptions();

            LatLng markerPosition = new LatLng(selectedSection.latitude, selectedSection.longitude);

            markerOptions.position(markerPosition);
            markerOptions.title(WordUtils.capitalizeFully(selectedSection.name));

            if (currentMarker != null) {
                currentMarker.remove();
                currentMarker = null;
            }
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));

                currentMarker = googleMap.addMarker(markerOptions);
                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        View view = getLayoutInflater().inflate(R.layout.pill_select_list_item, null);
                        MaterialCardView materialCardView = view.findViewById(R.id.card_view);
                        materialCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_primary));
                        TextView textView = view.findViewById(R.id.title);
                        textView.setText(marker.getTitle());
                        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_white));
                        return view;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
                currentMarker.showInfoWindow();

                calculateDirections(currentMarker);
            }
        } else {
            if (currentMarker != null) {
                currentMarker.remove();
                currentMarker = null;
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }
            }
        }
    }

    private void subscribeToViewModel() {
        mapsViewModel.getSections().observe(getViewLifecycleOwner(),
                sections -> {
                    sectionsListSelectorAdapter.submitList(sections);
                    redirectToArgsSectionIdOnce();
                });
    }

    private void redirectToArgsSectionIdOnce() {
        if (this.argsSectionId != -1) {
            if (sectionsListSelectorAdapter.getCurrentList() != null) {
                int sectionPosition = sectionsListSelectorAdapter.getCurrentList().stream()
                        .filter(section -> section.id == this.argsSectionId)
                        .map(section -> sectionsListSelectorAdapter.getCurrentList().indexOf(section))
                        .findFirst().orElse(-1);
                if (sectionPosition != -1) {
                    this.argsSectionId = -1;
                    sectionsListSelectorAdapter.setSelectedPosition(sectionPosition);
                }
            }
        }
    }

    private void setupFloatingActionButtons() {
        initFloatingActionButtonStates();

        floatingActionButtonSwitchOverlay.init(
                R.drawable.ic_layers_clear_black_24dp,
                "Ascunde secțiile",
                R.drawable.ic_layers_black_24dp,
                "Arată secțiile"
        );

        floatingActionButtonSwitchOverlay.setOnSwitchListener((v, isActive) -> {
            if (isActive) {
                setTileVisibility(0, true);
            } else {
                floatingActionButtonSwitchDetails.setActive(false);
                setTileVisibility(0, false);
            }
        });

        floatingActionButtonSwitchDetails.init(
                R.drawable.ic_hide_eye_black_24dp,
                "Arată nedetaliat",
                R.drawable.ic_show_eye_black_24dp,
                "Arată detaliat"
        );

        floatingActionButtonSwitchDetails.setOnSwitchListener((v, isActive) -> {
            if (isActive) {
                floatingActionButtonSwitchOverlay.setActive(true);
                setTileVisibility(1, true);
            } else {
                setTileVisibility(1, false);
            }
        });
    }

    private void initFloatingActionButtonStates() {
        floatingActionButtonSwitchOverlay.setActive(true);
        floatingActionButtonSwitchDetails.setActive(false);
    }

    private void setupMapView(@Nullable Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gpsChecker.onActivityResult(requestCode, resultCode, data);
    }

    private void setupPermissionHelpers() {
        locationPermissionHelper = new LocationPermissionHelper(this, new PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener((location) -> currentUserLocation = location);
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                mapView.getMapAsync(MapsFragment.this);
            }

            @Override
            public void onPermissionDenied() {
                openPlantsListFragment();
            }
        });
        gpsChecker = new GPSChecker(this, new PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                locationPermissionHelper.request();
            }

            @Override
            public void onPermissionDenied() {
                openPlantsListFragment();
            }
        });
        googleServicesChecker = new GoogleServicesChecker(this);
    }

    private void openPlantsListFragment() {
        NavDirections direction = MapsFragmentDirections
                .actionNavigationMapsToNavigationPlantsList();

        navController.navigate(direction);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        googleMap = map;
        storagePermissionHelper = new StoragePermissionHelper(this, new PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                setupTileOverlays();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(getContext(), "Couldn't load tile overlays.", Toast.LENGTH_SHORT).show();
            }
        });
        storagePermissionHelper.request();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(Constants.GARDEN_BOUNDS, 0));

        initFloatingActionButtonStates();

        // set up on click listeners
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapClickListener(this);

        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
        }

        selectSection();
    }

    private void calculateDirections(Marker marker) {
        Log.d(TAG, "calculating directions. " + currentUserLocation + " " + marker);
        if (currentUserLocation != null && marker != null) {
            com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                    marker.getPosition().latitude,
                    marker.getPosition().longitude
            );
            DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);
            directions.alternatives(false);
            directions.mode(TravelMode.WALKING);
            directions.origin(
                    new com.google.maps.model.LatLng(
                            currentUserLocation.getLatitude(),
                            currentUserLocation.getLongitude()
                    )
            );
            Log.d(TAG, "destination: " + destination.toString());
            directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
                @Override
                public void onResult(DirectionsResult result) {
                    Log.d(TAG, "routes: " + result.routes[0].toString());
                    Log.d(TAG, "geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                    addPolylinesToMap(result);
                }

                @Override
                public void onFailure(Throwable e) {
                    Log.e(TAG, "onFailure: " + e.getMessage());

                }
            });
        }
    }

    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(() -> {

            for (DirectionsRoute route : result.routes) {
                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                List<LatLng> newDecodedPath = new ArrayList<>();

                for (com.google.maps.model.LatLng latLng : decodedPath) {

                    newDecodedPath.add(new LatLng(
                            latLng.lat,
                            latLng.lng
                    ));
                }

                currentPath = newDecodedPath;

                if (currentPolyline != null) {
                    currentPolyline.remove();
                }
                currentPolyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                if(getContext() != null) {
                    currentPolyline.setColor(ContextCompat.getColor(getContext(), R.color.color_primary_light));
                }
                currentPolyline.setClickable(false);
                currentPolyline.setZIndex(1000);
                currentPolyline.setWidth(30);
                currentPolyline.setPattern(Arrays.asList(new Gap(20), new Dot()));

            }
        });
    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (sectionsListSelectorAdapter != null) {
            sectionsListSelectorAdapter.clearSelectedItem();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (sectionsListSelectorAdapter != null) {
            if (sectionsListSelectorAdapter.getSelectedItem() != null) {
                Section section = sectionsListSelectorAdapter.getSelectedItem();
                openSectionInfoActivity(section.id);
            }
        }
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        this.onMarkerClick(marker);
    }

    private void openSectionInfoActivity(int sectionId) {
        Intent intent = new Intent(getActivity(), SectionInfoActivity.class);
        intent.putExtra("sectionId", sectionId);
        startActivity(intent);
        onPause();
    }

    private void setupTileOverlays() {
        tileOverlays = new ArrayList<>();
        for (String relativePath : tileOverlaysRelativePaths) {
            TileProvider tileProvider = new ExpandedMBTilesTileProvider(
                    new MBTilesInternalStorageProxi(getContext())
                            .getProxi(relativePath),
                    256,
                    256);
            TileOverlay tileOverlay = googleMap.addTileOverlay(
                    new TileOverlayOptions()
                            .tileProvider(tileProvider));
            if (!relativePath.equals(tileOverlaysRelativePaths[0])) {
                tileOverlay.setVisible(false);
            }
            tileOverlays.add(tileOverlay);
        }
    }

    private void setTileVisibility(int index, boolean show) {
        if (tileOverlays != null) {
            TileOverlay tileOverlay = tileOverlays.get(index);
            tileOverlay.setVisible(show);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(Constants.MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(Constants.MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleServicesChecker.check()) {
            gpsChecker.asyncCheck();
        } else {
            openPlantsListFragment();
        }
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    private void clearTiles() {
        if (tileOverlays != null) {
            for (TileOverlay tileOverlay : tileOverlays) {
                tileOverlay.remove();
            }
            tileOverlays = null;
        }
    }

    @Override
    public void onStop() {
        clearTiles();
        mapView.onStop();
        super.onStop();
    }


    @Override
    public void onPause() {
        clearTiles();
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        clearTiles();
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }
}
