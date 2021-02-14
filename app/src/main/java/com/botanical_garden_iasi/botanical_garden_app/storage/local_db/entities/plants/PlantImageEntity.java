package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;

@Entity(
        tableName = "plant_images",
        indices = {
                @Index("plantId"),
                @Index(value = {"plantId", "url"}, unique = true)
        },
        foreignKeys = @ForeignKey(
                entity = PlantEntity.class,
                parentColumns = "id",
                childColumns = "plantId",
                onDelete = ForeignKey.CASCADE
        )
)
public class PlantImageEntity extends BaseEntity {

    public int plantId;

    public String url;

    public PlantImageEntity(int id, int plantId, String url) {
        this.id = id;
        this.plantId = plantId;
        this.url = url;
    }
}
