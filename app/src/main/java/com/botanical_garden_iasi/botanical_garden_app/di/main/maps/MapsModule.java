package com.botanical_garden_iasi.botanical_garden_app.di.main.maps;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.sections.IasiBotanicalGardenSectionsApi;
import com.botanical_garden_iasi.botanical_garden_app.repositories.sections.SectionRepository;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.IasiBotanicalGardenDatabase;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SubSectionDao;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class MapsModule {
    @MapsScope
    @Provides
    static SectionDao providesSectionDao(IasiBotanicalGardenDatabase database) {
        return database.sectionDao();
    }

    @MapsScope
    @Provides
    static SectionImageDao providesSectionImageDao(IasiBotanicalGardenDatabase database) {
        return database.sectionImageDao();
    }

    @MapsScope
    @Provides
    static SubSectionDao providesSubSectionDao(IasiBotanicalGardenDatabase database) {
        return database.subSectionDao();
    }

    @MapsScope
    @Provides
    static SectionRepository providesSectionRepository(IasiBotanicalGardenSectionsApi api,
                                                       SectionDao plantDao,
                                                       SubSectionDao subSectionDao,
                                                       SectionImageDao sectionImageDao, AppExecutors executors) {
        return new SectionRepository(api, plantDao, subSectionDao, sectionImageDao, executors);
    }
}
