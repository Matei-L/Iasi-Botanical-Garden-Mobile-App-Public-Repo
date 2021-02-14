package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoSettingEntity;

import java.util.List;

@Dao
public abstract class OtherPlantInfoSettingDao extends BaseDao<OtherPlantInfoSettingEntity> {

    @Query("SELECT * from other_plant_info_settings ORDER BY LOWER(title) ")
    public abstract LiveData<List<OtherPlantInfoSettingEntity>> getAll();

    @Query("UPDATE other_plant_info_settings SET hidden = :hidden WHERE id = :id")
    public abstract void setHidden(int id, boolean hidden);

    @Query("SELECT id FROM other_plant_info_settings WHERE LOWER(title) = LOWER(:title)")
    public abstract int getIdForTitle(String title);

    @Transaction
    @Query("SELECT * FROM other_plant_info_settings WHERE id IN (:ids)")
    public abstract List<OtherPlantInfoSettingEntity> getAllOtherPlantInfoForIds(List<Integer> ids);
}
