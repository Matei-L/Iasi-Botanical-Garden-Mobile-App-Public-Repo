package com.botanical_garden_iasi.botanical_garden_app.ui.settings.plants_settings_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.settings.PlantDescriptionSettings;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.SettingsListAdapter;
import com.botanical_garden_iasi.botanical_garden_app.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PlantsSettingsFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private PlantsSettingsViewModel plantsSettingsViewModel;

    private List<PlantDescriptionSettings> plantDescriptionSettings;
    private RecyclerView plantDescriptionsSettingsListRecyclerView;
    private SettingsListAdapter plantDescriptionsSettingsListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plantsSettingsViewModel = new ViewModelProvider(this, providerFactory)
                .get(PlantsSettingsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plants_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plantDescriptionSettings = new ArrayList<>();
        plantDescriptionsSettingsListAdapter = new SettingsListAdapter(plantDescriptionSettings);
        plantDescriptionsSettingsListRecyclerView = view.findViewById(R.id.plants_settings_descriptions_recycler_view);
        plantDescriptionsSettingsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        plantDescriptionsSettingsListRecyclerView.setAdapter(plantDescriptionsSettingsListAdapter);

        subscribeToViewModels();
        setupOnClickListeners();

    }

    private void setupOnClickListeners() {
        plantDescriptionsSettingsListAdapter.setOnClickListeners((position, isChecked) -> {
            plantDescriptionSettings.get(position).show = isChecked;
            plantsSettingsViewModel.setHidden(plantDescriptionSettings.get(position));
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void subscribeToViewModels() {
        plantsSettingsViewModel.getPlantDescriptionsSettings().observe(this, otherPlantInfoListResource -> {


            if (otherPlantInfoListResource != null) {
                switch (otherPlantInfoListResource.getStatus()) {
                    case LOADING:
                    case SUCCESS:
                        if (otherPlantInfoListResource.getData() != null) {
                            displaySettings(otherPlantInfoListResource.getData());
                        }
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), "Network error: " + otherPlantInfoListResource.getMessage(), Toast.LENGTH_SHORT).show();
                        if (otherPlantInfoListResource.getData() != null) {
                            displaySettings(otherPlantInfoListResource.getData());
                        }
                        break;
                }
            }
        });
    }

    private void displaySettings(List<PlantDescriptionSettings> otherPlantInfoList) {
        plantDescriptionSettings.clear();
        plantDescriptionSettings.addAll(otherPlantInfoList);
        plantDescriptionsSettingsListAdapter.notifyDataSetChanged();
    }
}
