package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;

@Entity(
        tableName = "other_plant_info",
        indices = {
                @Index("title"),
                @Index("plantId"),
                @Index("settingId"),
                @Index(value = {"plantId", "title"}, unique = true)
        },
        foreignKeys = {
                @ForeignKey(
                        entity = PlantEntity.class,
                        parentColumns = "id",
                        childColumns = "plantId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = OtherPlantInfoSettingEntity.class,
                        parentColumns = "id",
                        childColumns = "settingId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class OtherPlantInfoEntity extends BaseEntity {

    public int plantId;

    public int settingId;

    public String title;

    public String description;

    @Ignore
    public OtherPlantInfoSettingEntity settingEntity;

    public OtherPlantInfoEntity(int id, int plantId, int settingId, String title, String description) {
        this.id = id;
        this.plantId = plantId;
        this.settingId = settingId;
        this.title = title;
        this.description = description;
    }

    @Ignore
    public OtherPlantInfoEntity(int id, int plantId, int settingId, String title, String description,
                                OtherPlantInfoSettingEntity settingEntity) {
        this.id = id;
        this.plantId = plantId;
        this.settingId = settingId;
        this.title = title;
        this.description = description;
        this.settingEntity = settingEntity;
    }

    public boolean isEmpty() {
        return title == null && description == null;
    }
}
