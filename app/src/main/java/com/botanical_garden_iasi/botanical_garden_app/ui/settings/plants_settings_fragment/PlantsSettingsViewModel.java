package com.botanical_garden_iasi.botanical_garden_app.ui.settings.plants_settings_fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.botanical_garden_iasi.botanical_garden_app.network.Resource;
import com.botanical_garden_iasi.botanical_garden_app.repositories.plants.OtherPlantInfoRepository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.settings.PlantDescriptionSettings;

import java.util.List;

import javax.inject.Inject;

public class PlantsSettingsViewModel extends ViewModel {

    private OtherPlantInfoRepository otherPlantInfoRepository;
    private LiveData<Resource<List<PlantDescriptionSettings>>> plantDescriptionsSettings = null;

    @Inject
    PlantsSettingsViewModel(OtherPlantInfoRepository otherPlantInfoRepository) {
        this.otherPlantInfoRepository = otherPlantInfoRepository;
    }

    LiveData<Resource<List<PlantDescriptionSettings>>> getPlantDescriptionsSettings() {
        plantDescriptionsSettings = otherPlantInfoRepository.getAllAsSettings();
        return plantDescriptionsSettings;
    }

    void setHidden(PlantDescriptionSettings plantDescriptionSettings) {
        otherPlantInfoRepository
                .setOtherPlantInfoHidden(plantDescriptionSettings);
    }
}
