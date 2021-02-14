package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Embedded;
import androidx.room.Relation;

public class FullOtherPlantInfoEntity {
    @Embedded
    public OtherPlantInfoEntity otherPlantInfoEntity;

    @Relation(
            parentColumn = "settingId",
            entityColumn = "id"
    )
    public OtherPlantInfoSettingEntity settingEntity;
}
