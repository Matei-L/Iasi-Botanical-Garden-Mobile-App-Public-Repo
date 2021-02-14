package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.BaseEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionEntity;

import java.util.List;

@Entity(
        tableName = "plants",
        indices = {
                @Index("speciesId"),
                @Index("typeId")
        },
        foreignKeys = {
                @ForeignKey(
                        entity = SpeciesEntity.class,
                        parentColumns = "id",
                        childColumns = "speciesId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = TypeEntity.class,
                        parentColumns = "id",
                        childColumns = "typeId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = SectionEntity.class,
                        parentColumns = "id",
                        childColumns = "sectionId",
                        onDelete = ForeignKey.SET_NULL)
        }
)
public class PlantEntity extends BaseEntity {

    public int speciesId;

    public int typeId;

    public Integer sectionId;

    public String ipen;

    public String popularName;

    public String scientificName;

    public String author;

    public String origin;

    public String naturalArea;

    public boolean favorite;

    @Ignore
    public SpeciesEntity species;

    @Ignore
    public TypeEntity type;

    @Ignore
    public List<OtherPlantInfoEntity> descriptions;

    @Ignore
    public List<PlantImageEntity> images;

    public PlantEntity(int id, Integer sectionId, int speciesId, int typeId, String ipen, String popularName,
                       String scientificName, String author, String origin, String naturalArea) {
        this.id = id;
        this.sectionId = sectionId;
        this.speciesId = speciesId;
        this.typeId = typeId;
        this.ipen = ipen;
        this.popularName = popularName;
        this.scientificName = scientificName;
        this.author = author;
        this.origin = origin;
        this.naturalArea = naturalArea;
    }

    @Ignore
    public PlantEntity(int id, Integer sectionId, String ipen, String popularName, String scientificName,
                       String author, String origin, String naturalArea, boolean favorite) {
        this.id = id;
        this.ipen = ipen;
        this.sectionId = sectionId;
        this.popularName = popularName;
        this.scientificName = scientificName;
        this.author = author;
        this.origin = origin;
        this.naturalArea = naturalArea;
        this.favorite = favorite;
    }

    @Ignore
    public PlantEntity(int id, Integer sectionId, int speciesId, int typeId, String ipen, String popularName,
                       String scientificName, String author, String origin, String naturalArea,
                       boolean favorite, SpeciesEntity species, TypeEntity type,
                       List<OtherPlantInfoEntity> descriptions, List<PlantImageEntity> images,
                       long createdAt, long modifiedAt) {
        this.id = id;
        this.sectionId = sectionId;
        this.speciesId = speciesId;
        this.typeId = typeId;
        this.ipen = ipen;
        this.popularName = popularName;
        this.scientificName = scientificName;
        this.author = author;
        this.origin = origin;
        this.naturalArea = naturalArea;
        this.favorite = favorite;
        this.species = species;
        this.type = type;
        this.descriptions = descriptions;
        this.images = images;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}