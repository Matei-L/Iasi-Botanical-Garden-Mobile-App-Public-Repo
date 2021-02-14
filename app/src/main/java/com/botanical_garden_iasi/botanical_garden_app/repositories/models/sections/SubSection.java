package com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;

public class SubSection extends BaseModel {

    public int sectionId;

    public String name;

    public String description;

    public SubSection(int id, int sectionId, String name,
                      String description, long createdAt, long modifiedAt) {
        this.id = id;
        this.sectionId = sectionId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
