package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantImageEntity;

import java.util.List;

@Dao
public abstract class PlantImageDao extends BaseDao<PlantImageEntity> {

    @Query("SELECT * FROM plant_images WHERE plantId = :plantId")
    public abstract LiveData<List<PlantImageEntity>> getAllImagesFor(int plantId);

    @Query("SELECT * FROM plant_images WHERE plantId = :plantId")
    public abstract List<PlantImageEntity> getAllImagesRawFor(int plantId);

    @Query("SELECT * FROM plant_images WHERE plantId IN (:plantIds)")
    public abstract List<PlantImageEntity> getAllImagesRawFor(List<Integer> plantIds);
}
