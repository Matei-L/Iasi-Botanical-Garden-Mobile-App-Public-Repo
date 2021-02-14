package com.botanical_garden_iasi.botanical_garden_app.repositories.plants;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;
import com.botanical_garden_iasi.botanical_garden_app.helpers.mappers.PlantsMapper;
import com.botanical_garden_iasi.botanical_garden_app.network.BoundaryCallback;
import com.botanical_garden_iasi.botanical_garden_app.network.NetworkBoundResource;
import com.botanical_garden_iasi.botanical_garden_app.network.Resource;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.ApiResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.plants.IasiBotanicalGardenPlantsApi;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.plants.PlantResponse;
import com.botanical_garden_iasi.botanical_garden_app.repositories.Repository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.Plant;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoSettingDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.PlantDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.PlantImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.SpeciesDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.TypeDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoSettingEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantImageEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.SpeciesEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.TypeEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import javax.inject.Inject;

import retrofit2.Call;

public class PlantRepository extends Repository{
    private static final String TAG = "PlantRepository";
    private IasiBotanicalGardenPlantsApi api;
    private PlantDao plantDao;
    private TypeDao typeDao;
    private SpeciesDao speciesDao;
    private OtherPlantInfoDao otherPlantInfoDao;
    private OtherPlantInfoSettingDao otherPlantInfoSettingDao;
    private PlantImageDao plantImageDao;

    @Inject
    public PlantRepository(IasiBotanicalGardenPlantsApi api,
                           PlantDao plantDao, TypeDao typeDao, SpeciesDao speciesDao,
                           OtherPlantInfoDao otherPlantInfoDao,
                           OtherPlantInfoSettingDao otherPlantInfoSettingDao,
                           PlantImageDao plantImageDao, AppExecutors executors) {
        this.api = api;
        this.executors = executors;
        this.plantDao = plantDao;
        this.typeDao = typeDao;
        this.speciesDao = speciesDao;
        this.otherPlantInfoDao = otherPlantInfoDao;
        this.otherPlantInfoSettingDao = otherPlantInfoSettingDao;
        this.plantImageDao = plantImageDao;
    }

    public LiveData<Resource<Plant>> getPlant(int plantID) {
        return new NetworkBoundResource<Plant, PlantResponse>(executors) {
            @Override
            protected void onFetchFailed() {
                Log.e(TAG, "Fetching the plant wit id " + plantID + " failed.");
            }

            @Override
            protected void saveCallResult(PlantResponse plantResponse) {
                if (plantResponse != null) {

                    PlantEntity plantEntity = PlantsMapper.map(plantResponse);

                    upsert(plantEntity);

                    Log.i(TAG, "Saved the plant with id " + plantID + " into our local database.");
                }
            }

            @Override
            protected boolean shouldFetch(Plant plant) {
                boolean fetch = PlantRepository.this.shouldFetch(plant);
                Log.i(TAG, "shouldFetch plant " + (plant != null ? plant.id : "<<new plant>>") + ": " + fetch);
                return fetch;
            }

            @Override
            protected LiveData<Plant> loadFromDb() {
                return Transformations.map(plantDao.getById(plantID), PlantsMapper::map);
            }

            @Override
            protected LiveData<ApiResponse<PlantResponse>> createCall() {
                LiveData<ApiResponse<PlantResponse>> call = api.getPlant(plantID);
                Log.i(TAG, "Called the api for info related to the plant with id " + plantID);
                return call;
            }
        }.asLiveData();
    }

    public LiveData<PagedList<Plant>> getPlants(boolean forceFetch, String keyword, int optionIndex) {

        Log.i(TAG, "getPlants: keyword: " + keyword + " optionIndex " + optionIndex);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(Constants.VERTICAL_PAGE_SIZE / 2)
                .setPageSize(Constants.VERTICAL_PAGE_SIZE)
                .setPrefetchDistance(Constants.VERTICAL_PAGE_SIZE / 2)
                .build();

        Plant.FilterOptions option = Plant.FilterOptions.values()[optionIndex];
        DataSource.Factory<Integer, PlantEntity> datasource;
        datasource = getDataSourceForFilter(keyword, option);

        return new LivePagedListBuilder<>(datasource.map(PlantsMapper::map), config)
                .setBoundaryCallback(new BoundaryCallback<PlantResponse, Plant>(Constants.VERTICAL_PAGE_SIZE) {
                    @Override
                    public Executor getExecutor() {
                        return executors.io();
                    }

                    @Override
                    public Call<List<PlantResponse>> createCall(int offset, int limit) {
                        Log.i(TAG, "Plants BoundaryCallback createCall: offset(" + offset + "), limit(" + limit + ").");
                        return getAPICallForFilter(keyword, option, offset, limit);
                    }

                    @Override
                    public void saveResponses(List<PlantResponse> plantResponses, int offset, int limit) {
                        if (plantResponses != null && plantResponses.size() > 0) {
                            List<PlantEntity> plantEntities = plantResponses
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList());

                            List<Integer> plantEntityIds = plantEntities
                                    .stream()
                                    .map(o -> o.id)
                                    .collect(Collectors.toList());

                            List<PlantEntity> plantEntitiesToDelete =
                                    getPlantsPageRaw(keyword, option, offset, limit)
                                            .stream()
                                            .filter(otd -> !plantEntityIds.contains(otd.id))
                                            .collect(Collectors.toList());
                            Log.i(TAG, "upsert: " + plantEntitiesToDelete.size() +
                                    " plants to delete for offset "
                                    + offset + " limit "
                                    + limit);

                            plantDao.delete(plantEntitiesToDelete);

                            upsert(plantEntities);

                            Log.i(TAG, "Saved all " +
                                    plantEntities.size() + " plants for offset(" +
                                    offset + ") and limit(" + limit +
                                    ") into our local database.");
                        }
                    }

                    @Override
                    public boolean shouldFetch(List<Plant> items) {
                        boolean fetch = PlantRepository.this.shouldFetch(items);
                        Log.i(TAG, "plants shouldFetch: " + fetch + " forceFetch: " + forceFetch);
                        return fetch || forceFetch;
                    }

                    @Override
                    protected List<Plant> loadPageRawFromDb(int offset, int limit) {
                        return getPlantsPageRaw(keyword, option, offset, limit)
                                .stream()
                                .map(PlantsMapper::map)
                                .collect(Collectors.toList());
                    }

                    @Override
                    protected int getPage(Plant plant, int limit) {
                        Log.i(TAG, "getPage: getting page for " + plant.id);
                        return getPlantRowNumForWithFilter(plant.popularName, keyword, option) / limit;
                    }
                }).build();
    }

    public LiveData<PagedList<Plant>> getSimilarPlantsFor(Plant plant) {

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(Constants.HORIZONTAL_PAGE_SIZE / 2)
                .setPageSize(Constants.HORIZONTAL_PAGE_SIZE)
                .setPrefetchDistance(Constants.HORIZONTAL_PAGE_SIZE / 2)
                .build();

        return new LivePagedListBuilder<>(plantDao.getSimilarPlantsFor(plant.id, plant.type.id)
                .map(PlantsMapper::map), config)
                .setBoundaryCallback(new BoundaryCallback<PlantResponse, Plant>(Constants.HORIZONTAL_PAGE_SIZE) {
                    @Override
                    public Executor getExecutor() {
                        return executors.io();
                    }

                    @Override
                    public Call<List<PlantResponse>> createCall(int offset, int limit) {
                        Log.i(TAG, "Similar plants BoundaryCallback createCall: offset(" + offset + "), limit(" + limit + ").");
                        return api.getPlantsRawFrom(
                                plant.type.id,
                                offset,
                                limit);
                    }

                    @Override
                    public void saveResponses(List<PlantResponse> plantResponses, int offset, int limit) {
                        if (plantResponses != null && plantResponses.size() > 0) {
                            List<PlantEntity> plantEntities = plantResponses
                                    .stream()
                                    .filter(plantResponse ->
                                            plantResponse != null && plantResponse.id != plant.id)
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList());

                            List<Integer> plantEntityIds = plantEntities
                                    .stream()
                                    .map(o -> o.id)
                                    .collect(Collectors.toList());

                            List<PlantEntity> plantEntitiesToDelete =
                                    plantDao.getPlantsPageRawFor(plant.type.id, offset, limit)
                                            .stream()
                                            .filter(otd -> !plantEntityIds.contains(otd.id) && otd.id != plant.id)
                                            .collect(Collectors.toList());
                            Log.i(TAG, "Saved all " +
                                    plantEntities.size() + " plants for offset(" +
                                    offset + ") and limit(" + limit +
                                    ") into our local database.");

                            plantDao.delete(plantEntitiesToDelete);

                            upsert(plantEntities);

                            Log.i(TAG, "Saved all the similar plants into our local database. " + plantEntities.size());
                        }
                    }

                    @Override
                    public boolean shouldFetch(List<Plant> plants) {
                        boolean fetch = PlantRepository.this.shouldFetch(plants);
                        Log.i(TAG, "similar plants shouldFetch: " + fetch);
                        return fetch;
                    }

                    @Override
                    protected List<Plant> loadPageRawFromDb(int offset, int limit) {
                        return plantDao.getPlantsPageRawFor(plant.type.id, offset, limit)
                                .stream()
                                .map(PlantsMapper::map)
                                .collect(Collectors.toList());
                    }

                    @Override
                    protected int getPage(Plant plant, int limit) {
                        Log.i(TAG, "getPage: getting page for " + plant.id);
                        return plantDao.getPlantRowNumFor(plant.popularName, plant.type.name,
                                null, null, null, null, null) / limit;
                    }
                }).build();
    }

    public void setFavorite(int plantId, boolean favorite) {
        executors.io().execute(() -> plantDao.setFavorite(plantId, favorite)
        );
    }

    private void upsert(PlantEntity plantEntity) {
        typeDao.upsert(plantEntity.type);

        speciesDao.upsert(plantEntity.species);

        plantDao.upsert(plantEntity);

        List<OtherPlantInfoEntity> otherPlantInfoEntities = plantEntity.descriptions;

        List<Integer> otherPlantInfoEntityIds = otherPlantInfoEntities
                .stream()
                .map(o -> o.id)
                .collect(Collectors.toList());
        List<OtherPlantInfoEntity> otherPlantInfoEntitiesToDelete =
                otherPlantInfoDao.getAllOtherPlantInfoRawFor(plantEntity.id)
                        .stream()
                        .filter(otd -> !otherPlantInfoEntityIds.contains(otd.id))
                        .collect(Collectors.toList());

        otherPlantInfoDao.delete(otherPlantInfoEntitiesToDelete);

        resolveOtherPlantInfoSettingsDependencies(otherPlantInfoEntities);

        otherPlantInfoDao.upsert(otherPlantInfoEntities);

        List<PlantImageEntity> plantImageEntities = plantEntity.images;
        List<Integer> plantImageEntityIds = plantImageEntities
                .stream()
                .map(o -> o.id)
                .collect(Collectors.toList());
        List<PlantImageEntity> plantImageEntitiesToDelete =
                plantImageDao.getAllImagesRawFor(plantEntity.id)
                        .stream()
                        .filter(itd -> !plantImageEntityIds.contains(itd.id))
                        .collect(Collectors.toList());

        plantImageDao.delete(plantImageEntitiesToDelete);
        plantImageDao.upsert(plantImageEntities);
    }

    private void upsert(List<PlantEntity> plantEntities) {

        List<TypeEntity> typeEntities = plantEntities
                .stream()
                .map(p -> p.type)
                .collect(Collectors.toList());

        List<SpeciesEntity> speciesEntities = plantEntities
                .stream()
                .map(p -> p.species)
                .collect(Collectors.toList());


        typeDao.upsert(typeEntities);

        speciesDao.upsert(speciesEntities);

        plantDao.upsert(plantEntities);

        List<Integer> plantEntityIds = plantEntities
                .stream()
                .map(p -> p.id)
                .collect(Collectors.toList());

        List<OtherPlantInfoEntity> otherPlantInfoEntities = plantEntities
                .stream()
                .flatMap(p -> p.descriptions.stream())
                .collect(Collectors.toList());

        List<PlantImageEntity> plantImageEntities = plantEntities
                .stream()
                .flatMap(p -> p.images.stream())
                .collect(Collectors.toList());

        List<Integer> otherPlantInfoEntityIds = otherPlantInfoEntities
                .stream()
                .map(o -> o.id)
                .collect(Collectors.toList());
        List<OtherPlantInfoEntity> otherPlantInfoEntitiesToDelete =
                otherPlantInfoDao.getAllOtherPlantInfoRawFor(plantEntityIds)
                        .stream()
                        .filter(otd -> !otherPlantInfoEntityIds.contains(otd.id))
                        .collect(Collectors.toList());

        otherPlantInfoDao.delete(otherPlantInfoEntitiesToDelete);

        resolveOtherPlantInfoSettingsDependencies(otherPlantInfoEntities);

        otherPlantInfoDao.upsert(otherPlantInfoEntities);


        List<Integer> plantImageEntityIds = plantImageEntities
                .stream()
                .map(o -> o.id)
                .collect(Collectors.toList());
        List<PlantImageEntity> plantImageEntitiesToDelete =
                plantImageDao.getAllImagesRawFor(plantEntityIds)
                        .stream()
                        .filter(itd -> !plantImageEntityIds.contains(itd.id))
                        .collect(Collectors.toList());

        plantImageDao.delete(plantImageEntitiesToDelete);
        plantImageDao.upsert(plantImageEntities);
    }

    private void resolveOtherPlantInfoSettingsDependencies
            (List<OtherPlantInfoEntity> otherPlantInfoEntities) {
        otherPlantInfoEntities.forEach(otherPlantInfoEntity -> {
            int settingId = otherPlantInfoSettingDao.getIdForTitle(otherPlantInfoEntity.title);
            if (settingId == 0) {
                otherPlantInfoSettingDao.upsert(new OtherPlantInfoSettingEntity(settingId, otherPlantInfoEntity.title, false));
                settingId = otherPlantInfoSettingDao.getIdForTitle(otherPlantInfoEntity.title);
            }
            otherPlantInfoEntity.settingId = settingId;
        });
    }

    private DataSource.Factory<Integer, PlantEntity> getDataSourceForFilter(String keyword, Plant.FilterOptions option) {
        DataSource.Factory<Integer, PlantEntity> datasource;
        switch (option) {
            case ALL:
                datasource = plantDao.getAllWithKeyword(false, keyword);
                break;
            case FAVORITES:
                datasource = plantDao.getAllWithKeyword(true, keyword);
                break;
            case TYPE:
                datasource = plantDao.getAllBy(keyword, "", "",
                        "", "", "");
                break;
            case SPECIES:
                datasource = plantDao.getAllBy("", keyword, "",
                        "", "", "");
                break;
            case POPULAR_NAME:
                datasource = plantDao.getAllBy("", "", keyword,
                        "", "", "");
                break;
            case NATURAL_AREA:
                datasource = plantDao.getAllBy("", "", "",
                        keyword, "", "");
                break;
            case ORIGIN:
                datasource = plantDao.getAllBy("", "", "",
                        "", keyword, "");
                break;
            case OTHER_INFO_TITLE:
                datasource = plantDao.getAllBy("", "", "",
                        "", "", keyword);
                break;
            default:
                datasource = plantDao.getAllWithKeyword(false, "");
                break;
        }
        return datasource;
    }


    private Call<List<PlantResponse>> getAPICallForFilter(String keyword, Plant.FilterOptions option,
                                                          int offset, int limit) {
        Call<List<PlantResponse>> call;
        switch (option) {
            case ALL:
                call = api.getPlantsRawWithKeyword(keyword, offset, limit);
                break;
            case FAVORITES:
                call = null;
                break;
            case TYPE:
                call = api.getPlantsRawBy(keyword, "", "",
                        "", "", "", offset, limit);
                break;
            case SPECIES:
                call = api.getPlantsRawBy("", keyword, "",
                        "", "", "", offset, limit);
                break;
            case POPULAR_NAME:
                call = api.getPlantsRawBy("", "", keyword,
                        "", "", "", offset, limit);
                break;
            case NATURAL_AREA:
                call = api.getPlantsRawBy("", "", "",
                        keyword, "", "", offset, limit);
                break;
            case ORIGIN:
                call = api.getPlantsRawBy("", "", "",
                        "", keyword, "", offset, limit);
                break;
            case OTHER_INFO_TITLE:
                call = api.getPlantsRawBy("", "", "",
                        "", "", keyword, offset, limit);
                break;
            default:
                call = api.getPlantsRawWithKeyword("", offset, limit);
                break;
        }
        return call;
    }

    private List<PlantEntity> getPlantsPageRaw(String keyword, Plant.FilterOptions option,
                                               int offset, int limit) {
        List<PlantEntity> plantsPageRaw;
        switch (option) {
            case ALL:
                plantsPageRaw = plantDao.getPlantsPageRawWithKeyword(false, keyword, offset, limit);
                break;
            case FAVORITES:
                plantsPageRaw = plantDao.getPlantsPageRawWithKeyword(true, keyword, offset, limit);
                break;
            case TYPE:
                plantsPageRaw = plantDao.getPlantsPageRawBy(keyword, "", "",
                        "", "", "", offset, limit);
                break;
            case SPECIES:
                plantsPageRaw = plantDao.getPlantsPageRawBy("", keyword, "",
                        "", "", "", offset, limit);
                break;
            case POPULAR_NAME:
                plantsPageRaw = plantDao.getPlantsPageRawBy("", "", keyword,
                        "", "", "", offset, limit);
                break;
            case NATURAL_AREA:
                plantsPageRaw = plantDao.getPlantsPageRawBy("", "", "",
                        keyword, "", "", offset, limit);
                break;
            case ORIGIN:
                plantsPageRaw = plantDao.getPlantsPageRawBy("", "", "",
                        "", keyword, "", offset, limit);
                break;
            case OTHER_INFO_TITLE:
                plantsPageRaw = plantDao.getPlantsPageRawBy("", "", "",
                        "", "", keyword, offset, limit);
                break;
            default:
                plantsPageRaw = plantDao.getPlantsPageRawWithKeyword(false, "", offset, limit);
                break;
        }

        Log.i(TAG, "getPlantsPageRaw: pagesize: " + plantsPageRaw.size() + " option: " + option);

        return plantsPageRaw;
    }

    private int getPlantRowNumForWithFilter(String targetPopularName, String keyword, Plant.FilterOptions option) {
        int rowNum;
        switch (option) {
            case ALL:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, false, keyword);
                break;
            case FAVORITES:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, true, keyword);
                break;
            case TYPE:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, keyword, "", "",
                        "", "", "");
                break;
            case SPECIES:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, "", keyword, "",
                        "", "", "");
                break;
            case POPULAR_NAME:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, "", "", keyword,
                        "", "", "");
                break;
            case NATURAL_AREA:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, "", "", "",
                        keyword, "", "");
                break;
            case ORIGIN:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, "", "", "",
                        "", keyword, "");
                break;
            case OTHER_INFO_TITLE:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, "", "", "",
                        "", "", keyword);
                break;
            default:
                rowNum = plantDao.getPlantRowNumFor(targetPopularName, false, "");
                break;
        }

        return rowNum;
    }
}
