package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;

import java.util.List;

@Entity(
        tableName = "sections"
)
public class SectionEntity extends BaseEntity {
    public String name;

    public String locationAndSize;

    public String description;

    public double latitude;

    public double longitude;

    @Ignore
    public List<SubSectionEntity> subsections;

    @Ignore
    public List<SectionImageEntity> images;

    public SectionEntity(int id, String name, String locationAndSize, String description, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.locationAndSize = locationAndSize;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public SectionEntity(int id, String name, String locationAndSize, String description, double latitude, double longitude,
                         List<SubSectionEntity> subsections, List<SectionImageEntity> images) {
        this.id = id;
        this.name = name;
        this.locationAndSize = locationAndSize;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subsections = subsections;
        this.images = images;
    }

    public SectionEntity(int id, String name, String locationAndSize, String description, double latitude, double longitude,
                         List<SubSectionEntity> subsections, List<SectionImageEntity> images, long createdAt, long modifiedAt) {
        this.id = id;
        this.name = name;
        this.locationAndSize = locationAndSize;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subsections = subsections;
        this.images = images;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
