package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionImageEntity;

import java.util.List;

@Dao
public abstract class SectionImageDao extends BaseDao<SectionImageEntity> {

    @Query("SELECT * FROM section_images WHERE sectionId = :sectionId")
    public abstract LiveData<List<SectionImageEntity>> getAllImagesFor(int sectionId);

    @Query("SELECT * FROM section_images WHERE sectionId = :sectionId")
    public abstract List<SectionImageEntity> getAllImagesRawFor(int sectionId);

    @Query("SELECT * FROM section_images WHERE sectionId IN (:sectionIds)")
    public abstract List<SectionImageEntity> getAllImagesRawFor(List<Integer> sectionIds);
}
