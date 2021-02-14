package com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plants_list_fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.botanical_garden_iasi.botanical_garden_app.repositories.plants.PlantRepository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.Plant;

import javax.inject.Inject;


public class PlantsListViewModel extends ViewModel {
    private static final String TAG = "PlantsListViewModel";

    @Inject
    PlantRepository plantRepository;

    private LiveData<PagedList<Plant>> plants;
    private int currentOption;
    private String currentKeyword;

    @Inject
    PlantsListViewModel(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
        plants = null;
        clearFilters();
    }

    public void setFilters(String keyword, int option) {
        currentOption = option;
        currentKeyword = keyword;
    }

    public void clearFilters() {
        currentOption = 0;
        currentKeyword = "";
    }

    public int getCurrentOption() {
        return currentOption;
    }

    public String getCurrentKeyword() {
        return currentKeyword;
    }

    LiveData<PagedList<Plant>> getPlants() {
        if (plants != null) {
            return plants;
        }
        return getPlants(false);
    }

    LiveData<PagedList<Plant>> getPlants(boolean forceRefresh) {
        plants = plantRepository.getPlants(forceRefresh, currentKeyword, currentOption);
        return plants;
    }
}
