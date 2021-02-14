package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoEntity;

import java.util.List;
import java.util.stream.Collectors;

@Dao
public abstract class OtherPlantInfoDao extends BaseDao<OtherPlantInfoEntity> {

    @Query("SELECT * FROM other_plant_info WHERE plantId = :plantId")
    public abstract List<OtherPlantInfoEntity> getAllOtherPlantInfoRawFor(int plantId);

    @Transaction
    @Query("SELECT * FROM other_plant_info WHERE plantId IN (:plantIds)")
    public abstract List<OtherPlantInfoEntity> getAllOtherPlantInfoRawFor(List<Integer> plantIds);


    @Override
    public long insert(OtherPlantInfoEntity otherPlantInfoEntity) {
        if (otherPlantInfoEntity.isEmpty()) {
            otherPlantInfoEntity = null;
        }
        return super.insert(otherPlantInfoEntity);
    }

    @Override
    public List<Long> insert(List<OtherPlantInfoEntity> otherPlantInfoEntities) {
        otherPlantInfoEntities = otherPlantInfoEntities
                .stream()
                .filter(otherPlantInfoEntity -> !otherPlantInfoEntity.isEmpty())
                .collect(Collectors.toList());
        return super.insert(otherPlantInfoEntities);
    }

    @Override
    public void upsert(OtherPlantInfoEntity otherPlantInfoEntity) {
        if (!otherPlantInfoEntity.isEmpty()) {
            super.upsert(otherPlantInfoEntity);
        }
    }

    @Override
    public void upsert(List<OtherPlantInfoEntity> otherPlantInfoEntities) {
        otherPlantInfoEntities = otherPlantInfoEntities
                .stream()
                .filter(otherPlantInfoEntity -> !otherPlantInfoEntity.isEmpty())
                .collect(Collectors.toList());
        super.upsert(otherPlantInfoEntities);
    }

}
