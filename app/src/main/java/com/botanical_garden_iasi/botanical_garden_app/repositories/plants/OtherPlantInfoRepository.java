package com.botanical_garden_iasi.botanical_garden_app.repositories.plants;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.helpers.mappers.PlantsMapper;
import com.botanical_garden_iasi.botanical_garden_app.network.NetworkBoundResource;
import com.botanical_garden_iasi.botanical_garden_app.network.Resource;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.ApiResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.plants.IasiBotanicalGardenPlantsApi;
import com.botanical_garden_iasi.botanical_garden_app.repositories.Repository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.settings.PlantDescriptionSettings;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoSettingDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoSettingEntity;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class OtherPlantInfoRepository extends Repository {
    private static final String TAG = "OtherPlantInfoRepositor";

    private IasiBotanicalGardenPlantsApi api;
    private OtherPlantInfoDao otherPlantInfoDao;
    private OtherPlantInfoSettingDao otherPlantInfoSettingDao;

    @Inject
    public OtherPlantInfoRepository(IasiBotanicalGardenPlantsApi api,
                                    OtherPlantInfoDao otherPlantInfoDao,
                                    OtherPlantInfoSettingDao otherPlantInfoSettingDao,
                                    AppExecutors executors) {
        this.api = api;
        this.executors = executors;
        this.otherPlantInfoDao = otherPlantInfoDao;
        this.otherPlantInfoSettingDao = otherPlantInfoSettingDao;
    }

    public LiveData<Resource<List<PlantDescriptionSettings>>> getAllAsSettings() {

        return new NetworkBoundResource<List<PlantDescriptionSettings>, List<String>>(executors) {

            @Override
            protected void onFetchFailed() {

            }

            @Override
            protected void saveCallResult(List<String> titles) {

                List<OtherPlantInfoSettingEntity> otherPlantInfoSettingEntities =
                        titles
                                .stream()
                                .map(title -> new OtherPlantInfoSettingEntity(0, title, false))
                                .collect(Collectors.toList());

                upsert(otherPlantInfoSettingEntities);

                Log.i(TAG, "Saved otherPlantInfoSettings to our database.");
            }

            @Override
            protected boolean shouldFetch(List<PlantDescriptionSettings> plantDescriptionSettings) {
                boolean fetch = OtherPlantInfoRepository.this.shouldFetch(plantDescriptionSettings);
                Log.i(TAG, "shouldFetch: " + fetch);
                return fetch;
            }

            @Override
            protected LiveData<List<PlantDescriptionSettings>> loadFromDb() {
                return Transformations.map(otherPlantInfoSettingDao.getAll(),
                        otherPlantInfoAsSettings ->
                                otherPlantInfoAsSettings.stream()
                                        .map(PlantsMapper::map)
                                        .collect(Collectors.toList()));
            }

            @Override
            protected LiveData<ApiResponse<List<String>>> createCall() {
                LiveData<ApiResponse<List<String>>> call = api.getOtherPlantInfoDistinctTitles();
                Log.i(TAG, "Called the api for PlantDescriptionsSettings");
                return call;
            }
        }.asLiveData();
    }

    public void setOtherPlantInfoHidden(PlantDescriptionSettings plantDescriptionSettings) {
        executors.io().execute(() -> {
                    OtherPlantInfoSettingEntity otherPlantInfoSettingEntity = PlantsMapper.map(plantDescriptionSettings);
                    otherPlantInfoSettingDao.setHidden(otherPlantInfoSettingEntity.id, otherPlantInfoSettingEntity.hidden);
                }
        );
    }

    private void upsert(List<OtherPlantInfoSettingEntity> otherPlantInfoSettingEntities) {
        List<Integer> otherPlantInfoSettingEntityIds = otherPlantInfoSettingEntities
                .stream()
                .map(p -> p.id)
                .collect(Collectors.toList());

        List<OtherPlantInfoSettingEntity> otherPlantInfoSettingEntitiesToDelete =
                otherPlantInfoSettingDao.getAllOtherPlantInfoForIds(otherPlantInfoSettingEntityIds)
                        .stream()
                        .filter(otd -> !otherPlantInfoSettingEntityIds.contains(otd.id))
                        .collect(Collectors.toList());

        otherPlantInfoSettingDao.delete(otherPlantInfoSettingEntitiesToDelete);

        otherPlantInfoSettingDao.upsert(otherPlantInfoSettingEntities);
    }

}
