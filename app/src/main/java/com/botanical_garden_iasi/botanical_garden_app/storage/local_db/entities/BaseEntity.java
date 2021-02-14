package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities;

import androidx.room.PrimaryKey;

public abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public long createdAt;

    public long modifiedAt;
}
