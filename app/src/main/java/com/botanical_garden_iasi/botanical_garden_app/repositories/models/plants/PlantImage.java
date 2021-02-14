package com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;

public class PlantImage extends BaseModel {

    public int plantId;

    public String url;

    public PlantImage(int id, int plantId, String url, long createdAt, long modifiedAt) {
        this.id = id;
        this.plantId = plantId;
        this.url = url;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
