package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;

import java.util.List;

@Entity(
        tableName = "type",
        indices = {
                @Index(value = {"name"}, unique = true)
        }
)
public class TypeEntity extends BaseEntity {

    public String name;

    @Ignore
    public List<SpeciesEntity> species;

    @Ignore
    public List<PlantEntity> plants;

    public TypeEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public TypeEntity(int id, String name, List<SpeciesEntity> species) {
        this.id = id;
        this.name = name;
        this.species = species;
    }

    @Ignore
    public TypeEntity(int id, String name, List<SpeciesEntity> species, List<PlantEntity> plants) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.plants = plants;
    }
}
