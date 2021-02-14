package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;

import java.util.List;

@Entity(
        tableName = "species",
        indices = {
                @Index("typeId")
        },
        foreignKeys = {
                @ForeignKey(
                        entity = TypeEntity.class,
                        parentColumns = "id",
                        childColumns = "typeId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class SpeciesEntity extends BaseEntity {

    public int typeId;

    public String name;

    @Ignore
    public TypeEntity type;

    @Ignore
    public List<PlantEntity> plants;

    public SpeciesEntity(int id, int typeId, String name) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;
    }

    @Ignore
    public SpeciesEntity(int id, int typeId, String name, TypeEntity type) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;
        this.type = type;
    }

    @Ignore
    public SpeciesEntity(int id, int typeId, String name, TypeEntity type, List<PlantEntity> plants) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;
        this.type = type;
        this.plants = plants;
    }
}
