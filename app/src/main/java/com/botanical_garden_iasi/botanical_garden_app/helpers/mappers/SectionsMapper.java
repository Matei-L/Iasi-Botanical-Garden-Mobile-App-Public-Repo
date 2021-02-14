package com.botanical_garden_iasi.botanical_garden_app.helpers.mappers;

import android.util.Log;

import com.botanical_garden_iasi.botanical_garden_app.network.responses.sections.SectionImageResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.sections.SectionResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.sections.SubSectionResponse;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.SectionImage;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.SubSection;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.FullSectionEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionImageEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SubSectionEntity;

import java.util.stream.Collectors;

public class SectionsMapper {
    private static final String TAG = "SectionsMapper";

    public static SectionEntity map(SectionResponse sectionResponse) {
        if (sectionResponse != null) {
            return new SectionEntity(
                    sectionResponse.id,
                    sectionResponse.name,
                    sectionResponse.locationAndSize,
                    sectionResponse.description,
                    sectionResponse.latitude,
                    sectionResponse.longitude,
                    sectionResponse.subsections != null ?
                            sectionResponse.subsections
                                    .stream()
                                    .map(s -> map(s, sectionResponse.id))
                                    .collect(Collectors.toList()) : null,
                    sectionResponse.images != null ?
                            sectionResponse.images
                                    .stream()
                                    .map(oi -> map(oi, sectionResponse.id))
                                    .collect(Collectors.toList()) : null
            );
        }
        return null;
    }


    public static Section map(SectionEntity sectionEntity) {
        if (sectionEntity != null) {
            Log.d(TAG, "map: modifiedAt" + sectionEntity.modifiedAt);
            return new Section(
                    sectionEntity.id,
                    sectionEntity.name,
                    sectionEntity.locationAndSize,
                    sectionEntity.description,
                    sectionEntity.latitude,
                    sectionEntity.longitude,
                    sectionEntity.subsections != null ?
                            sectionEntity.subsections
                                    .stream()
                                    .map(SectionsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    sectionEntity.images != null ?
                            sectionEntity.images
                                    .stream()
                                    .map(SectionsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    sectionEntity.createdAt,
                    sectionEntity.modifiedAt
            );
        }
        return null;
    }

    public static SubSection map(SubSectionEntity subSectionEntity) {
        if (subSectionEntity != null) {
            return new SubSection(
                    subSectionEntity.id,
                    subSectionEntity.sectionId,
                    subSectionEntity.name,
                    subSectionEntity.description,
                    subSectionEntity.createdAt,
                    subSectionEntity.modifiedAt

            );
        }
        return null;
    }

    public static SectionEntity map(FullSectionEntity fullSectionEntity) {
        if (fullSectionEntity != null) {
            return new SectionEntity(
                    fullSectionEntity.section.id,
                    fullSectionEntity.section.name,
                    fullSectionEntity.section.locationAndSize,
                    fullSectionEntity.section.description,
                    fullSectionEntity.section.latitude,
                    fullSectionEntity.section.longitude,
                    fullSectionEntity.subsections,
                    fullSectionEntity.images,
                    fullSectionEntity.section.createdAt,
                    fullSectionEntity.section.modifiedAt
            );
        }
        return null;
    }

    public static SectionEntity map(Section section) {
        if (section != null) {
            return new SectionEntity(
                    section.id,
                    section.name,
                    section.locationAndSize,
                    section.description,
                    section.latitude,
                    section.longitude,
                    section.subsections != null ?
                            section.subsections.stream()
                                    .map(SectionsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    section.images != null ?
                            section.images.stream()
                                    .map(SectionsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    section.createdAt,
                    section.modifiedAt
            );
        }
        return null;
    }

    public static SubSectionEntity map(SubSection subSection) {
        if (subSection != null) {
            return new SubSectionEntity(
                    subSection.id,
                    subSection.sectionId,
                    subSection.name,
                    subSection.description
            );
        }
        return null;
    }

    public static SectionImageEntity map(SectionImage sectionImage) {
        if (sectionImage != null) {
            return new SectionImageEntity(
                    sectionImage.id,
                    sectionImage.sectionId,
                    sectionImage.url
            );
        }
        return null;
    }

    public static SectionImage map(SectionImageEntity sectionImageEntity) {
        if (sectionImageEntity != null) {
            return new SectionImage(
                    sectionImageEntity.id,
                    sectionImageEntity.sectionId,
                    sectionImageEntity.url,
                    sectionImageEntity.createdAt,
                    sectionImageEntity.modifiedAt
            );
        }
        return null;
    }

    public static SubSectionEntity map(SubSectionResponse subSectionResponse,
                                       int sectionId) {
        if (subSectionResponse != null) {
            return new SubSectionEntity(
                    subSectionResponse.id,
                    sectionId,
                    subSectionResponse.name,
                    subSectionResponse.description
            );
        }
        return null;
    }

    public static SectionImageEntity map(SectionImageResponse sectionImageResponse, int sectionId) {
        if (sectionImageResponse != null) {
//            String url = StringUtils.chop(IASI_BOTANICAL_GARDEN_API_URL) + sectionImageResponse.url;
            String url = sectionImageResponse.url;
            return new SectionImageEntity(
                    sectionImageResponse.id,
                    sectionId,
                    url
            );
        }
        return null;
    }

}
