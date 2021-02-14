package com.botanical_garden_iasi.botanical_garden_app.repositories.models;

import androidx.annotation.Nullable;

public abstract class ItemModel extends BaseModel {
    public abstract String getTitle();

    public abstract String getSubTitle();

    public abstract String getIconUrl();

    @Override
    public abstract boolean equals(@Nullable Object obj);
}
