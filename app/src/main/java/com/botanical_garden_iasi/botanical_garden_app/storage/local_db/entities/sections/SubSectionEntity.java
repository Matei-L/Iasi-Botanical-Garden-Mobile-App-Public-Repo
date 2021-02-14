package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;

@Entity(
        tableName = "subsections",
        indices = {
                @Index("sectionId")
        },
        foreignKeys = {
                @ForeignKey(
                        entity = SectionEntity.class,
                        parentColumns = "id",
                        childColumns = "sectionId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class SubSectionEntity extends BaseEntity {

    public int sectionId;

    public String name;

    public String description;

    public SubSectionEntity(int id, int sectionId, String name, String description) {
        this.id = id;
        this.sectionId = sectionId;
        this.name = name;
        this.description = description;
    }

    public boolean isEmpty() {
        return name == null && description == null;
    }
}
