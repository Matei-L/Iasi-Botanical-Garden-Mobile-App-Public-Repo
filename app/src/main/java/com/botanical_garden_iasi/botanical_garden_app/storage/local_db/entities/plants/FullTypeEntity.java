package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FullTypeEntity {
    @Embedded
    public TypeEntity type;

    @Relation(
            parentColumn = "id",
            entityColumn = "typeId"
    )
    public List<FullSpeciesEntity> plants;
}
