package com.botanical_garden_iasi.botanical_garden_app.ui.settings.user_settings_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;
import com.botanical_garden_iasi.botanical_garden_app.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class UserSettingsFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private SharedPreferences cachingSharedPref;
    private UserSettingsViewModel userSettingsViewModel;
    private Spinner spinner;
    private ArrayList<CacheSpinnerItem> cacheSpinnerItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSettingsViewModel = new ViewModelProvider(this, providerFactory)
                .get(UserSettingsViewModel.class);
        setUpCacheSpinnerItems();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.cache_settings_spinner);

        cachingSharedPref = view.getContext()
                .getSharedPreferences("Caching", Context.MODE_PRIVATE);

        setupSpinner(view);
    }

    private void setupSpinner(@NonNull View view) {
        ArrayAdapter<CacheSpinnerItem> spinnerAdapter =
                new ArrayAdapter<>(view.getContext(), R.layout.simple_spinner_item, cacheSpinnerItems);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        long cacheTTL = Constants.getCacheTTLMilis();
        for (int i = 0; i < cacheSpinnerItems.size(); i++) {
            if (cacheSpinnerItems.get(i).getTime() == cacheTTL) {
                spinner.setSelection(i);
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long cacheTTL = cacheSpinnerItems.get(position).getTime();
                SharedPreferences.Editor editor = cachingSharedPref.edit();
                editor.putLong(getString(R.string.caching_ttl), cacheTTL);
                Constants.setCacheTTLMilis(cacheTTL);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setUpCacheSpinnerItems() {
        cacheSpinnerItems = new ArrayList<>();
        cacheSpinnerItems.add(new CacheSpinnerItem("1 SEC", TimeUnit.SECONDS.toMillis(1)));
        cacheSpinnerItems.add(new CacheSpinnerItem("10 SEC", TimeUnit.SECONDS.toMillis(10)));
        cacheSpinnerItems.add(new CacheSpinnerItem("1 MIN", TimeUnit.MINUTES.toMillis(1)));
        cacheSpinnerItems.add(new CacheSpinnerItem("10 MIN", TimeUnit.MINUTES.toMillis(10)));
        cacheSpinnerItems.add(new CacheSpinnerItem("1 ORĂ", TimeUnit.HOURS.toMillis(1)));
        cacheSpinnerItems.add(new CacheSpinnerItem("1 ZI", TimeUnit.DAYS.toMillis(1)));
        cacheSpinnerItems.add(new CacheSpinnerItem("3 ZILE", TimeUnit.DAYS.toMillis(3)));
    }
}
