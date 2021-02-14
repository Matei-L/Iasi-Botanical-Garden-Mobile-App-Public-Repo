package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SubSectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Dao
public abstract class SubSectionDao extends BaseDao<SubSectionEntity> {

    @Query("SELECT * FROM subsections WHERE sectionId = :sectionId")
    public abstract List<SubSectionEntity> getAllSubsectionsRawFor(int sectionId);

    @Transaction
    @Query("SELECT * FROM subsections WHERE sectionId IN (:sectionIds)")
    public abstract List<SubSectionEntity> getAllSubsectionsRawFor(List<Integer> sectionIds);


    @Override
    public long insert(SubSectionEntity subSectionEntity) {
        if (subSectionEntity.isEmpty()) {
            subSectionEntity = null;
        }
        return super.insert(subSectionEntity);
    }

    @Override
    public List<Long> insert(List<SubSectionEntity> subSectionEntities) {
        subSectionEntities = subSectionEntities
                .stream()
                .filter(subSectionEntity -> !subSectionEntity.isEmpty())
                .collect(Collectors.toList());
        return super.insert(subSectionEntities);
    }

    @Override
    public void upsert(SubSectionEntity subSectionEntity) {
        if (!subSectionEntity.isEmpty()) {
            super.upsert(subSectionEntity);
        }
    }

    @Override
    public void upsert(List<SubSectionEntity> subSectionEntities) {
        subSectionEntities = subSectionEntities
                .stream()
                .filter(subSectionEntity -> !subSectionEntity.isEmpty())
                .collect(Collectors.toList());
        super.upsert(subSectionEntities);
    }

}
