package com.botanical_garden_iasi.botanical_garden_app.repositories.plants;

import androidx.lifecycle.LiveData;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.plants.IasiBotanicalGardenPlantsApi;
import com.botanical_garden_iasi.botanical_garden_app.repositories.Repository;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.PlantImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantImageEntity;

import java.util.List;

import javax.inject.Inject;

public class PlantImageRepository extends Repository {
    private IasiBotanicalGardenPlantsApi api;
    private PlantImageDao plantImageDao;

    @Inject
    public PlantImageRepository(IasiBotanicalGardenPlantsApi api,
                                PlantImageDao plantImageDao,
                                AppExecutors executors) {
        this.api = api;
        this.executors = executors;
        this.plantImageDao = plantImageDao;
    }

    public LiveData<List<PlantImageEntity>> getImagesFor(int plantID) {
        return plantImageDao.getAllImagesFor(plantID);
    }
}
