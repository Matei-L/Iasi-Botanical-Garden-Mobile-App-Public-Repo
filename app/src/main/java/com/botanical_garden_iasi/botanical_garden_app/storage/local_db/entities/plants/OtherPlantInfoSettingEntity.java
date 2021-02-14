package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;


@Entity(
        tableName = "other_plant_info_settings",
        indices = {
                @Index(value = {"title"}, unique = true)
        }
)
public class OtherPlantInfoSettingEntity extends BaseEntity {

    public String title;

    public boolean hidden;

    public OtherPlantInfoSettingEntity(int id, String title, boolean hidden) {
        this.id = id;
        this.title = title.toLowerCase();
        this.hidden = hidden;
    }

    @Ignore
    public OtherPlantInfoSettingEntity(int id, String title, boolean hidden,
                                       long createdAt, long modifiedAt) {
        this.id = id;
        this.title = title.toLowerCase();
        this.hidden = hidden;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
