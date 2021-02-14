package com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections;

import androidx.annotation.Nullable;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.ItemModel;

import java.util.List;

public class Section extends ItemModel {

    public String name;

    public String locationAndSize;

    public String description;

    public double latitude;

    public double longitude;

    public List<SubSection> subsections;

    public List<SectionImage> images;

    public Section(int id, String name, String locationAndSize, String description, double latitude,
                   double longitude, List<SubSection> subsections, List<SectionImage> images,
                   long createdAt, long modifiedAt) {
        this.id = id;
        this.name = name;
        this.locationAndSize = locationAndSize;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subsections = subsections;
        this.images = images;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSubTitle() {
        return "";
    }

    @Override
    public String getIconUrl() {
        return images != null ? (images.size() > 0 ? images.get(0).url : "") : "";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Section)) {
            return false;
        }
        Section otherSection = (Section) obj;
        return otherSection.name.equals(name) &&
                otherSection.locationAndSize.equals(locationAndSize) &&
                otherSection.description.equals(description) &&
                otherSection.latitude == latitude &&
                otherSection.longitude == longitude &&
                otherSection.subsections.equals(subsections) &&
                otherSection.images == images;
    }

    public enum FilterOptions {
        NAME
    }
}
