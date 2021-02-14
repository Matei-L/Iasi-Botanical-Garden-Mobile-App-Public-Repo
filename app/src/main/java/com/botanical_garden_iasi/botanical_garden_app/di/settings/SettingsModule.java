package com.botanical_garden_iasi.botanical_garden_app.di.settings;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.plants.IasiBotanicalGardenPlantsApi;
import com.botanical_garden_iasi.botanical_garden_app.repositories.plants.OtherPlantInfoRepository;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.IasiBotanicalGardenDatabase;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoSettingDao;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class SettingsModule {

    @SettingsScope
    @Provides
    static OtherPlantInfoDao providesOtherPlantInfoDao(IasiBotanicalGardenDatabase database) {
        return database.otherPlantInfoDao();
    }

    @SettingsScope
    @Provides
    static OtherPlantInfoSettingDao providesOtherPlantInfoSettingDao(IasiBotanicalGardenDatabase database) {
        return database.otherPlantInfoSettingDao();
    }

    @SettingsScope
    @Provides
    static OtherPlantInfoRepository providesOtherPlantInfoRepository(IasiBotanicalGardenPlantsApi api,
                                                                     OtherPlantInfoDao otherPlantInfoDao,
                                                                     OtherPlantInfoSettingDao otherPlantInfoSettingDao,
                                                                     AppExecutors executors) {
        return new OtherPlantInfoRepository(api, otherPlantInfoDao, otherPlantInfoSettingDao, executors);
    }

}
