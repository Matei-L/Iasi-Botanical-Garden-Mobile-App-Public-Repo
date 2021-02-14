package com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;

import java.util.List;

public class Type extends BaseModel {
    public int id;
    public String name;
    public List<Species> species;
    public List<Plant> plants;

    public Type(int id, String name, List<Species> species, List<Plant> plants, long createdAt, long modifiedAt) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.plants = plants;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
