package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FullPlantEntity {
    @Embedded
    public PlantEntity plant;

    @Relation(
            parentColumn = "speciesId",
            entityColumn = "id"
    )
    public SpeciesEntity species;

    @Relation(
            parentColumn = "typeId",
            entityColumn = "id"
    )
    public TypeEntity type;

    @Relation(
            parentColumn = "id",
            entityColumn = "plantId",
            entity = OtherPlantInfoEntity.class
    )
    public List<FullOtherPlantInfoEntity> descriptions;

    @Relation(
            parentColumn = "id",
            entityColumn = "plantId"
    )
    public List<PlantImageEntity> images;

    public FullPlantEntity(PlantEntity plant, SpeciesEntity species,
                           TypeEntity type, List<FullOtherPlantInfoEntity> descriptions,
                           List<PlantImageEntity> images) {
        this.plant = plant;
        this.species = species;
        this.type = type;
        this.descriptions = descriptions;
        this.images = images;
    }
}