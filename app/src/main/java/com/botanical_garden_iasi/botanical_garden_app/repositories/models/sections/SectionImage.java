package com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;

public class SectionImage extends BaseModel {

    public int sectionId;

    public String url;

    public SectionImage(int id, int sectionId, String url, long createdAt, long modifiedAt) {
        this.id = id;
        this.sectionId = sectionId;
        this.url = url;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
