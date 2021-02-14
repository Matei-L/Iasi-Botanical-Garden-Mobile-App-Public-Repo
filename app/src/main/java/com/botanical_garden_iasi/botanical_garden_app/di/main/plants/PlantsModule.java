package com.botanical_garden_iasi.botanical_garden_app.di.main.plants;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.plants.IasiBotanicalGardenPlantsApi;
import com.botanical_garden_iasi.botanical_garden_app.repositories.plants.OtherPlantInfoRepository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.plants.PlantImageRepository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.plants.PlantRepository;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.IasiBotanicalGardenDatabase;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoSettingDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.PlantDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.PlantImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.SpeciesDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.TypeDao;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class PlantsModule {

    @PlantsScope
    @Provides
    static PlantDao providesPlantDao(IasiBotanicalGardenDatabase database) {
        return database.plantDao();
    }

    @PlantsScope
    @Provides
    static PlantImageDao providesPlantImageDao(IasiBotanicalGardenDatabase database) {
        return database.plantImageDao();
    }

    @PlantsScope
    @Provides
    static OtherPlantInfoDao providesOtherPlantInfoDao(IasiBotanicalGardenDatabase database) {
        return database.otherPlantInfoDao();
    }

    @PlantsScope
    @Provides
    static OtherPlantInfoSettingDao providesOtherPlantInfoSettingDao(IasiBotanicalGardenDatabase database) {
        return database.otherPlantInfoSettingDao();
    }

    @PlantsScope
    @Provides
    static TypeDao providesTypeDao(IasiBotanicalGardenDatabase database) {
        return database.typeDao();
    }

    @PlantsScope
    @Provides
    static SpeciesDao providesSpeciesDao(IasiBotanicalGardenDatabase database) {
        return database.speciesDao();
    }

    @PlantsScope
    @Provides
    static PlantRepository providesPlantRepository(IasiBotanicalGardenPlantsApi api,
                                                   PlantDao plantDao, TypeDao typeDao, SpeciesDao speciesDao,
                                                   OtherPlantInfoDao otherPlantInfoDao,
                                                   OtherPlantInfoSettingDao otherPlantInfoSettingDao,
                                                   PlantImageDao plantImageDao, AppExecutors executors) {
        return new PlantRepository(api, plantDao, typeDao, speciesDao, otherPlantInfoDao, otherPlantInfoSettingDao, plantImageDao, executors);
    }

    @PlantsScope
    @Provides
    static OtherPlantInfoRepository providesOtherPlantInfoRepository(IasiBotanicalGardenPlantsApi api,
                                                                     OtherPlantInfoDao otherPlantInfoDao,
                                                                     OtherPlantInfoSettingDao otherPlantInfoSettingDao,
                                                                     AppExecutors executors) {
        return new OtherPlantInfoRepository(api, otherPlantInfoDao, otherPlantInfoSettingDao, executors);
    }

    @PlantsScope
    @Provides
    static PlantImageRepository providesPlantImageRepository(IasiBotanicalGardenPlantsApi api,
                                                             PlantImageDao plantImageDao,
                                                             AppExecutors executors) {
        return new PlantImageRepository(api, plantImageDao, executors);
    }
}
