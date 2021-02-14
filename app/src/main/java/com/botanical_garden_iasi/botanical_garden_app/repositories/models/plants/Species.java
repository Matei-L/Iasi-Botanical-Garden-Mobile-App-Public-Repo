package com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;

import java.util.List;

public class Species extends BaseModel {
    public String name;
    public Type type;
    public List<Plant> plants;

    public Species(int id, String name, Type type, List<Plant> plants, long createdAt, long modifiedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.plants = plants;
    }
}
