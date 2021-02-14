package com.botanical_garden_iasi.botanical_garden_app.helpers.mappers;

import android.util.Log;

import com.botanical_garden_iasi.botanical_garden_app.network.responses.plants.OtherPlantInfoResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.plants.PlantImageResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.plants.PlantResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.plants.SpeciesResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.plants.TypeResponse;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.Plant;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.PlantDescription;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.PlantImage;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.Species;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.plants.Type;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.settings.PlantDescriptionSettings;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.FullOtherPlantInfoEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.FullPlantEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.OtherPlantInfoSettingEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantImageEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.SpeciesEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.TypeEntity;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

import static com.botanical_garden_iasi.botanical_garden_app.helpers.Constants.IASI_BOTANICAL_GARDEN_API_URL;

public class PlantsMapper {
    private static final String TAG = "PlantsMapper";

    public static PlantEntity map(PlantResponse plantResponse) {
        if (plantResponse != null) {
            return new PlantEntity(
                    plantResponse.id,
                    plantResponse.sectionId,
                    plantResponse.species.id,
                    plantResponse.type.id,
                    plantResponse.ipen,
                    plantResponse.popularName,
                    plantResponse.scientificName,
                    plantResponse.author,
                    plantResponse.origin,
                    plantResponse.naturalArea,
                    false,
                    map(plantResponse.species, plantResponse.type.id),
                    map(plantResponse.type),
                    plantResponse.otherInfo != null ?
                            plantResponse.otherInfo
                                    .stream()
                                    .map(oi -> map(oi, plantResponse.id))
                                    .collect(Collectors.toList()) : null,
                    plantResponse.images != null ?
                            plantResponse.images
                                    .stream()
                                    .map(oi -> map(oi, plantResponse.id))
                                    .collect(Collectors.toList()) : null,
                    0,
                    0
            );
        }
        return null;
    }


    public static SpeciesEntity map(SpeciesResponse speciesResponse) {
        if (speciesResponse != null) {
            return new SpeciesEntity(
                    speciesResponse.id,
                    speciesResponse.type != null ?
                            speciesResponse.type.id : 0,
                    speciesResponse.name,
                    map(speciesResponse.type),
                    speciesResponse.plants != null ?
                            speciesResponse.plants
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null
            );
        }
        return null;
    }

    public static SpeciesEntity map(SpeciesResponse speciesResponse, int typeId) {
        if (speciesResponse != null) {
            return new SpeciesEntity(
                    speciesResponse.id,
                    typeId,
                    speciesResponse.name
            );
        }
        return null;
    }

    public static TypeEntity map(TypeResponse typeResponse) {
        if (typeResponse != null) {
            return new TypeEntity(
                    typeResponse.id,
                    typeResponse.name,
                    typeResponse.species != null ?
                            typeResponse.species
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    typeResponse.plants != null ?
                            typeResponse.plants
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null
            );
        }
        return null;
    }


    public static Plant map(PlantEntity plantEntity) {
        if (plantEntity != null) {
            Log.d(TAG, "map: modifiedAt" + plantEntity.modifiedAt);
            return new Plant(
                    plantEntity.id,
                    plantEntity.sectionId,
                    plantEntity.ipen,
                    map(plantEntity.species),
                    map(plantEntity.type),
                    plantEntity.popularName,
                    plantEntity.scientificName,
                    plantEntity.author,
                    plantEntity.origin,
                    plantEntity.naturalArea,
                    plantEntity.favorite,
                    plantEntity.descriptions != null ?
                            plantEntity.descriptions
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    plantEntity.images != null ?
                            plantEntity.images.stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    plantEntity.createdAt,
                    plantEntity.modifiedAt
            );
        }
        return null;
    }

    public static PlantDescription map(OtherPlantInfoEntity otherPlantInfoEntity) {
        if (otherPlantInfoEntity != null) {
            return new PlantDescription(
                    otherPlantInfoEntity.id,
                    otherPlantInfoEntity.plantId,
                    otherPlantInfoEntity.title,
                    otherPlantInfoEntity.description,
                    map(otherPlantInfoEntity.settingEntity),
                    otherPlantInfoEntity.createdAt,
                    otherPlantInfoEntity.modifiedAt

            );
        }
        return null;
    }

    public static Species map(SpeciesEntity speciesEntity) {
        if (speciesEntity != null) {
            return new Species(
                    speciesEntity.id,
                    speciesEntity.name,
                    map(speciesEntity.type),
                    speciesEntity.plants != null ?
                            speciesEntity.plants
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    speciesEntity.createdAt,
                    speciesEntity.modifiedAt
            );
        }
        return null;
    }

    public static Type map(TypeEntity typeEntity) {
        if (typeEntity != null) {
            return new Type(
                    typeEntity.id,
                    typeEntity.name,
                    typeEntity.species != null ?
                            typeEntity.species
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    typeEntity.plants != null ?
                            typeEntity.plants
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    typeEntity.createdAt,
                    typeEntity.modifiedAt
            );
        }
        return null;
    }

    public static PlantEntity map(FullPlantEntity fullPlantEntity) {
        if (fullPlantEntity != null) {
            return new PlantEntity(
                    fullPlantEntity.plant.id,
                    fullPlantEntity.plant.sectionId,
                    fullPlantEntity.plant.speciesId,
                    fullPlantEntity.plant.typeId,
                    fullPlantEntity.plant.ipen,
                    fullPlantEntity.plant.popularName,
                    fullPlantEntity.plant.scientificName,
                    fullPlantEntity.plant.author,
                    fullPlantEntity.plant.origin,
                    fullPlantEntity.plant.naturalArea,
                    fullPlantEntity.plant.favorite,
                    fullPlantEntity.species,
                    fullPlantEntity.type,
                    fullPlantEntity.descriptions != null ?
                            fullPlantEntity.descriptions
                                    .stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    fullPlantEntity.images,
                    fullPlantEntity.plant.createdAt,
                    fullPlantEntity.plant.modifiedAt
            );
        }
        return null;
    }

    public static OtherPlantInfoEntity map(FullOtherPlantInfoEntity fullOtherPlantInfoEntity) {
        if (fullOtherPlantInfoEntity != null) {
            return new OtherPlantInfoEntity(
                    fullOtherPlantInfoEntity.otherPlantInfoEntity.id,
                    fullOtherPlantInfoEntity.otherPlantInfoEntity.plantId,
                    fullOtherPlantInfoEntity.otherPlantInfoEntity.settingId,
                    fullOtherPlantInfoEntity.otherPlantInfoEntity.title,
                    fullOtherPlantInfoEntity.otherPlantInfoEntity.description,
                    fullOtherPlantInfoEntity.settingEntity

            );
        }
        return null;
    }

    public static PlantEntity map(Plant plant) {
        if (plant != null) {
            return new PlantEntity(
                    plant.id,
                    plant.sectionId,
                    plant.species.id,
                    plant.type.id,
                    plant.ipen,
                    plant.popularName,
                    plant.scientificName,
                    plant.author,
                    plant.origin,
                    plant.naturalArea,
                    plant.favorite,
                    map(plant.species),
                    map(plant.type),
                    plant.descriptions != null ?
                            plant.descriptions.stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    plant.images != null ?
                            plant.images.stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    plant.createdAt,
                    plant.modifiedAt
            );
        }
        return null;
    }

    private static SpeciesEntity map(Species species) {
        if (species != null) {
            return new SpeciesEntity(
                    species.id,
                    species.type != null ? species.type.id : 0,
                    species.name,
                    map(species.type),
                    species.plants != null ?
                            species.plants.stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null
            );
        }
        return null;
    }

    private static TypeEntity map(Type type) {
        if (type != null) {
            return new TypeEntity(
                    type.id,
                    type.name,
                    type.species != null ?
                            type.species.stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null,
                    type.plants != null ?
                            type.plants.stream()
                                    .map(PlantsMapper::map)
                                    .collect(Collectors.toList()) : null
            );
        }
        return null;
    }

    public static OtherPlantInfoEntity map(PlantDescription plantDescription) {
        if (plantDescription != null) {
            return new OtherPlantInfoEntity(
                    plantDescription.id,
                    plantDescription.plantId,
                    plantDescription.settingId,
                    plantDescription.title,
                    plantDescription.description
            );
        }
        return null;
    }

    public static PlantImageEntity map(PlantImage plantImage) {
        if (plantImage != null) {
            return new PlantImageEntity(
                    plantImage.id,
                    plantImage.plantId,
                    plantImage.url
            );
        }
        return null;
    }

    public static PlantImage map(PlantImageEntity plantImageEntity) {
        if (plantImageEntity != null) {
            return new PlantImage(
                    plantImageEntity.id,
                    plantImageEntity.plantId,
                    plantImageEntity.url,
                    plantImageEntity.createdAt,
                    plantImageEntity.modifiedAt
            );
        }
        return null;
    }

    public static OtherPlantInfoEntity map(OtherPlantInfoResponse otherPlantInfoResponse,
                                           int plantId) {
        if (otherPlantInfoResponse != null) {
            return new OtherPlantInfoEntity(
                    otherPlantInfoResponse.id,
                    plantId,
                    0,
                    otherPlantInfoResponse.title,
                    otherPlantInfoResponse.description
            );
        }
        return null;
    }

    public static PlantImageEntity map(PlantImageResponse plantImageResponse, int plantId) {
        if (plantImageResponse != null) {
            String url = StringUtils.chop(IASI_BOTANICAL_GARDEN_API_URL) + plantImageResponse.url;
            return new PlantImageEntity(
                    plantImageResponse.id,
                    plantId,
                    url
            );
        }
        return null;
    }

    public static PlantDescriptionSettings map(OtherPlantInfoSettingEntity otherPlantInfoSettingEntity) {
        if (otherPlantInfoSettingEntity != null) {
            return new PlantDescriptionSettings(
                    otherPlantInfoSettingEntity.id,
                    otherPlantInfoSettingEntity.title,
                    !otherPlantInfoSettingEntity.hidden,
                    otherPlantInfoSettingEntity.createdAt,
                    otherPlantInfoSettingEntity.modifiedAt
            );
        }
        return null;
    }

    public static OtherPlantInfoSettingEntity map(PlantDescriptionSettings plantDescriptionSettings) {
        if (plantDescriptionSettings != null) {
            return new OtherPlantInfoSettingEntity(
                    plantDescriptionSettings.id,
                    plantDescriptionSettings.title,
                    !plantDescriptionSettings.show,
                    plantDescriptionSettings.createdAt,
                    plantDescriptionSettings.modifiedAt
            );
        }
        return null;
    }

}
