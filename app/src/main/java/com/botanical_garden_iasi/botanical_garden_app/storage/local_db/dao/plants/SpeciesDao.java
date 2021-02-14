package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants;

import androidx.room.Dao;
import androidx.room.Query;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.SpeciesEntity;

import java.util.List;

@Dao
public abstract class SpeciesDao extends BaseDao<SpeciesEntity> {

    @Query("SELECT * FROM species WHERE typeId = :typeId ORDER BY id")
    abstract List<SpeciesEntity> getAllPlantsRawForType(int typeId);

    @Query("SELECT * FROM species WHERE typeId IN (:typeIds) ORDER BY id")
    abstract List<SpeciesEntity> getAllPlantsRawForType(List<Integer> typeIds);
}
