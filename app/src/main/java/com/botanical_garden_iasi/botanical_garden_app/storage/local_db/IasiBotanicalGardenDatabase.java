package com.botanical_garden_iasi.botanical_garden_app.storage.local_db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.OtherPlantInfoSettingDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.PlantDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.PlantImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.SpeciesDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants.TypeDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SubSectionDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoSettingEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantImageEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.SpeciesEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.TypeEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionImageEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SubSectionEntity;

@Database(
        entities = {
                PlantEntity.class,
                OtherPlantInfoEntity.class,
                OtherPlantInfoSettingEntity.class,
                PlantImageEntity.class,
                TypeEntity.class,
                SpeciesEntity.class,

                SectionEntity.class,
                SubSectionEntity.class,
                SectionImageEntity.class
        },
        version = 20
)
public abstract class IasiBotanicalGardenDatabase extends RoomDatabase {
    public abstract PlantDao plantDao();

    public abstract PlantImageDao plantImageDao();

    public abstract OtherPlantInfoDao otherPlantInfoDao();

    public abstract OtherPlantInfoSettingDao otherPlantInfoSettingDao();

    public abstract TypeDao typeDao();

    public abstract SpeciesDao speciesDao();



    public abstract SectionDao sectionDao();

    public abstract SubSectionDao subSectionDao();

    public abstract SectionImageDao sectionImageDao();
}
