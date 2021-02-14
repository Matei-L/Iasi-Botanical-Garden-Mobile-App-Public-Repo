package com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants;

import androidx.annotation.Nullable;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.ItemModel;

import java.util.List;

public class Plant extends ItemModel {
    public String ipen;
    public Integer sectionId;
    public Species species;
    public Type type;
    public String popularName;
    public String scientificName;
    public String author;
    public String origin;
    public String naturalArea;
    public boolean favorite;
    public List<PlantDescription> descriptions;
    public List<PlantImage> images;

    public Plant(int id, Integer sectionId, String ipen, Species species, Type type,
                 String popularName, String scientificName, String author, String origin,
                 String naturalArea, boolean favorite, List<PlantDescription> descriptions,
                 List<PlantImage> images, long createdAt, long modifiedAt) {
        this.id = id;

        // TODO: DELETE THIS IF
        if (popularName.equals("Tisă")) {
            this.sectionId = 9; // Sectia Biologica
        }
        else{
            this.sectionId = sectionId;
        }


        this.ipen = ipen;
        this.species = species;
        this.type = type;
        this.popularName = popularName;
        this.scientificName = scientificName;
        this.author = author;
        this.origin = origin;
        this.naturalArea = naturalArea;
        this.favorite = favorite;
        this.descriptions = descriptions;
        this.images = images;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Plant)) {
            return false;
        }
        Plant otherPlant = (Plant) obj;
        return otherPlant.ipen.equals(ipen) &&
                (otherPlant.sectionId != null && otherPlant.sectionId.equals(sectionId)) &&
                otherPlant.popularName.equals(popularName) &&
                otherPlant.scientificName.equals(scientificName) &&
                otherPlant.author.equals(author) &&
                otherPlant.origin.equals(origin) &&
                otherPlant.naturalArea.equals(naturalArea) &&
                otherPlant.favorite == favorite &&
                otherPlant.descriptions == descriptions &&
                otherPlant.images == images;
    }

    @Override
    public String getTitle() {
        return popularName;
    }

    @Override
    public String getSubTitle() {
        return scientificName;
    }

    @Override
    public String getIconUrl() {
        return images != null ? (images.size() > 0 ? images.get(0).url : "") : "";
    }

    public enum FilterOptions {
        ALL,
        FAVORITES,
        TYPE,
        SPECIES,
        POPULAR_NAME,
        NATURAL_AREA,
        ORIGIN,
        OTHER_INFO_TITLE

    }
}
