package com.botanical_garden_iasi.botanical_garden_app.repositories.models.settings;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.BaseModel;

public class PlantDescriptionSettings extends BaseModel implements SettingsItem {
    public String title;
    public boolean show;

    public PlantDescriptionSettings(int id, String title, boolean show, long createdAt, long modifiedAt) {
        this.id = id;
        this.title = title;
        this.show = show;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public boolean isChecked() {
        return show;
    }
}
