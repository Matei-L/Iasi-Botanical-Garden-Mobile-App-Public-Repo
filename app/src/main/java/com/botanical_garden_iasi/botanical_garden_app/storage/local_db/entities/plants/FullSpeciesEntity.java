package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FullSpeciesEntity {
    @Embedded
    public SpeciesEntity species;

    @Relation(
            parentColumn = "id",
            entityColumn = "speciesId"
    )
    public List<FullPlantEntity> plants;
}
