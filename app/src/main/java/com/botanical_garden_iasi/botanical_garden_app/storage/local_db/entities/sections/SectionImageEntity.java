package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;

@Entity(
        tableName = "section_images",
        indices = {
                @Index("sectionId"),
                @Index(value = {"sectionId", "url"}, unique = true)
        },
        foreignKeys = @ForeignKey(
                entity = SectionEntity.class,
                parentColumns = "id",
                childColumns = "sectionId",
                onDelete = ForeignKey.CASCADE
        )
)
public class SectionImageEntity extends BaseEntity {

    public int sectionId;

    public String url;

    public SectionImageEntity(int id, int sectionId, String url) {
        this.id = id;
        this.sectionId = sectionId;
        this.url = url;
    }
}
