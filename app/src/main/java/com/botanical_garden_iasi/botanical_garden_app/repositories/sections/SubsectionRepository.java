package com.botanical_garden_iasi.botanical_garden_app.repositories.sections;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.sections.IasiBotanicalGardenSectionsApi;
import com.botanical_garden_iasi.botanical_garden_app.repositories.Repository;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SubSectionDao;

import javax.inject.Inject;

public class SubsectionRepository extends Repository {
    private IasiBotanicalGardenSectionsApi api;
    private static final String TAG = "SubsectionRepository";

    private SubSectionDao subSectionDao;

    @Inject
    public SubsectionRepository(IasiBotanicalGardenSectionsApi api,
                                SubSectionDao subSectionDao,
                                AppExecutors executors) {
        this.api = api;
        this.executors = executors;
        this.subSectionDao = subSectionDao;
    }

}
