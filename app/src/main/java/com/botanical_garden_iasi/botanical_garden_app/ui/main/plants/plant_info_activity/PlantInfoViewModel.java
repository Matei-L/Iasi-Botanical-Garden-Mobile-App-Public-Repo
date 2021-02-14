package com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plant_info_activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.botanical_garden_iasi.botanical_garden_app.network.Resource;
import com.botanical_garden_iasi.botanical_garden_app.repositories.plants.OtherPlantInfoRepository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.plants.PlantRepository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.Plant;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.settings.PlantDescriptionSettings;

import javax.inject.Inject;

public class PlantInfoViewModel extends ViewModel {
    private static final String TAG = "PlantInformationViewMod";

    private PlantRepository plantRepository;
    private OtherPlantInfoRepository otherPlantInfoRepository;

    private LiveData<PagedList<Plant>> similarPlantsResourcePagedList = null;
    private LiveData<Resource<Plant>> plantResource = null;
    private int plantId;

    @Inject
    PlantInfoViewModel(PlantRepository plantRepository,
                       OtherPlantInfoRepository otherPlantInfoRepository) {
        this.plantRepository = plantRepository;
        this.otherPlantInfoRepository = otherPlantInfoRepository;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setFavorite(boolean favorite) {
        Resource<Plant> plantResource = this.plantResource.getValue();
        if (plantResource != null && plantResource.getData() != null) {
            plantRepository.setFavorite(plantResource.getData().id, favorite);
        }
    }

    LiveData<Resource<Plant>> getPlantResource() {
        if (plantResource == null) {
            this.plantResource = plantRepository.getPlant(this.plantId);
        }
        return plantResource;
    }

    void setOtherPlantInfoHidden(PlantDescriptionSettings settings, boolean hidden) {
        settings.show = !hidden;
        otherPlantInfoRepository.setOtherPlantInfoHidden(settings);
    }

    LiveData<PagedList<Plant>> getSimilarPlantsPagedList() {
        if (similarPlantsResourcePagedList == null) {
            if (plantResource.getValue() != null && plantResource.getValue().getData() != null) {
                Plant plant = plantResource.getValue().getData();
                similarPlantsResourcePagedList = plantRepository.getSimilarPlantsFor(plant);
            }
        }
        return similarPlantsResourcePagedList;
    }
}
