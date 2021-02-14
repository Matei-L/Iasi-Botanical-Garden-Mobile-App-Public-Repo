package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants;

import androidx.room.Dao;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.TypeEntity;

@Dao
public abstract class TypeDao extends BaseDao<TypeEntity> {
}
