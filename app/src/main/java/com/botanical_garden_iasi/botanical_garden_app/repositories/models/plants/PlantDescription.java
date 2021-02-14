package com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.settings.PlantDescriptionSettings;

public class PlantDescription extends BaseModel {

    public int plantId;

    public int settingId;

    public String title;

    public String description;

    public PlantDescriptionSettings settings;

    public PlantDescription(int id, int plantId,
                            String title, String description, PlantDescriptionSettings settings,
                            long createdAt, long modifiedAt) {
        this.id = id;
        this.plantId = plantId;
        this.title = title;
        this.description = description;
        this.settings = settings;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
