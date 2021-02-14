package com.botanical_garden_iasi.botanical_garden_app.repositories.sections;

import androidx.lifecycle.LiveData;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.sections.IasiBotanicalGardenSectionsApi;
import com.botanical_garden_iasi.botanical_garden_app.repositories.Repository;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionImageEntity;

import java.util.List;

import javax.inject.Inject;

public class SectionImageRepository extends Repository {
    private IasiBotanicalGardenSectionsApi api;
    private SectionImageDao sectionImageDao;

    @Inject
    public SectionImageRepository(IasiBotanicalGardenSectionsApi api,
                                  SectionImageDao sectionImageDao,
                                  AppExecutors executors) {
        this.api = api;
        this.executors = executors;
        this.sectionImageDao = sectionImageDao;
    }

    public LiveData<List<SectionImageEntity>> getImagesFor(int sectionID) {
        return sectionImageDao.getAllImagesFor(sectionID);
    }
}
