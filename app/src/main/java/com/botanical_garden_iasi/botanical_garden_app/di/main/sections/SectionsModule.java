package com.botanical_garden_iasi.botanical_garden_app.di.main.sections;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.sections.IasiBotanicalGardenSectionsApi;
import com.botanical_garden_iasi.botanical_garden_app.repositories.sections.SectionImageRepository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.sections.SectionRepository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.sections.SubsectionRepository;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.IasiBotanicalGardenDatabase;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SubSectionDao;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class SectionsModule {

    @SectionsScope
    @Provides
    static SectionDao providesSectionDao(IasiBotanicalGardenDatabase database) {
        return database.sectionDao();
    }

    @SectionsScope
    @Provides
    static SectionImageDao providesSectionImageDao(IasiBotanicalGardenDatabase database) {
        return database.sectionImageDao();
    }

    @SectionsScope
    @Provides
    static SubSectionDao providesSubSectionDao(IasiBotanicalGardenDatabase database) {
        return database.subSectionDao();
    }

    @SectionsScope
    @Provides
    static SectionRepository providesSectionRepository(IasiBotanicalGardenSectionsApi api,
                                                       SectionDao plantDao,
                                                       SubSectionDao subSectionDao,
                                                       SectionImageDao sectionImageDao, AppExecutors executors) {
        return new SectionRepository(api, plantDao, subSectionDao, sectionImageDao, executors);
    }

    @SectionsScope
    @Provides
    static SubsectionRepository providesSubsectionRepository(IasiBotanicalGardenSectionsApi api,
                                                             SubSectionDao subSectionDao,
                                                             AppExecutors executors) {
        return new SubsectionRepository(api, subSectionDao, executors);
    }

    @SectionsScope
    @Provides
    static SectionImageRepository providesSectionImageRepository(IasiBotanicalGardenSectionsApi api,
                                                                 SectionImageDao sectionImageDao,
                                                                 AppExecutors executors) {
        return new SectionImageRepository(api, sectionImageDao, executors);
    }
}
