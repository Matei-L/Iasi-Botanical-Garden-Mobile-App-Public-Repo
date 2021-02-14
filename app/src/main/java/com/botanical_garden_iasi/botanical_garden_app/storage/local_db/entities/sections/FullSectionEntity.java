package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FullSectionEntity {
    @Embedded
    public SectionEntity section;

    @Relation(
            parentColumn = "id",
            entityColumn = "sectionId",
            entity = SubSectionEntity.class
    )
    public List<SubSectionEntity> subsections;

    @Relation(
            parentColumn = "id",
            entityColumn = "sectionId"
    )
    public List<SectionImageEntity> images;

    public FullSectionEntity(SectionEntity section, List<SubSectionEntity> subsections, List<SectionImageEntity> images) {
        this.section = section;
        this.subsections = subsections;
        this.images = images;
    }
}
