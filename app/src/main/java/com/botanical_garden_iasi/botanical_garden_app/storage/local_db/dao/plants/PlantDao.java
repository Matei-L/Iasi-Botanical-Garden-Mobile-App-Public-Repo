package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.plants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.botanical_garden_iasi.botanical_garden_app.helpers.mappers.PlantsMapper;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.FullPlantEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.plants.PlantEntity;

import java.util.List;
import java.util.stream.Collectors;

@Dao
public abstract class PlantDao extends BaseDao<PlantEntity> {

    private static final String plantsByQuery = "SELECT DISTINCT plants.* " +
            "FROM plants JOIN type ON type.id = plants.typeId " +
            "JOIN species ON species.id = plants.speciesId " +
            "JOIN other_plant_info ON other_plant_info.plantId = plants.id  " +
            "WHERE LOWER(type.name) LIKE '%' || LOWER(:type) || '%' AND " +
            "LOWER(species.name) LIKE '%' || LOWER(:species) || '%' AND " +
            "LOWER(popularName) LIKE '%' || LOWER(:popularName) || '%' AND " +
            "LOWER(naturalArea) LIKE '%' || LOWER(:naturalArea) || '%' AND " +
            "LOWER(origin) LIKE '%' || LOWER(:origin) || '%' AND " +
            "LOWER(other_plant_info.title) LIKE '%' || LOWER(:otherInfosTitle) || '%' ";

    private static final String plantsWithKeywordQuery = "SELECT DISTINCT plants.* " +
            "FROM plants JOIN type ON type.id = plants.typeId " +
            "JOIN species ON species.id = plants.speciesId " +
            "JOIN other_plant_info ON other_plant_info.plantId = plants.id  " +
            "WHERE favorite >= :favorite AND (" +
            "LOWER(type.name) LIKE '%' || LOWER(:keyword) || '%' OR " +
            "LOWER(plants.ipen) LIKE '%' || LOWER(:keyword) || '%' OR " +
            "LOWER(plants.author) LIKE '%' || LOWER(:keyword) || '%' OR " +
            "LOWER(plants.scientificName) LIKE '%' || LOWER(:keyword) || '%' OR " +
            "LOWER(popularName) LIKE '%' || LOWER(:keyword) || '%' OR " +
            "LOWER(origin) LIKE '%' || LOWER(:keyword) || '%' OR " +
            "LOWER(other_plant_info.title) LIKE '%' || LOWER(:keyword) || '%' OR " +
            "LOWER(other_plant_info.description) LIKE '%' || LOWER(:keyword) || '%') ";

    @Transaction
    @Query("SELECT * FROM plants WHERE id = :plantId")
    abstract LiveData<FullPlantEntity> _loadById(int plantId);

    @Transaction
    @Query(plantsByQuery + "ORDER BY id LIMIT :limit OFFSET :offset")
    abstract List<FullPlantEntity> _loadPlantsPageRawBy(String type,
                                                        String species,
                                                        String popularName,
                                                        String naturalArea,
                                                        String origin,
                                                        String otherInfosTitle,
                                                        int offset,
                                                        int limit);

    @Transaction
    public List<PlantEntity> getPlantsPageRawBy(String type,
                                                String species,
                                                String popularName,
                                                String naturalArea,
                                                String origin,
                                                String otherInfosTitle,
                                                int offset,
                                                int limit) {
        return _loadPlantsPageRawBy(type,
                species,
                popularName,
                naturalArea,
                origin,
                otherInfosTitle,
                offset,
                limit)
                .stream()
                .map(PlantsMapper::map)
                .collect(Collectors.toList());
    }

    @Transaction
    @Query(plantsWithKeywordQuery +
            "ORDER BY id LIMIT :limit OFFSET :offset")
    abstract List<FullPlantEntity> _loadPlantsPageRawWithKeyword(Boolean favorite,
                                                                 String keyword,
                                                                 int offset,
                                                                 int limit);

    @Transaction
    public List<PlantEntity> getPlantsPageRawWithKeyword(Boolean favorite,
                                                         String keyword,
                                                         int offset,
                                                         int limit) {
        return _loadPlantsPageRawWithKeyword(favorite, keyword, offset, limit)
                .stream()
                .map(PlantsMapper::map)
                .collect(Collectors.toList());
    }


    @Query("SELECT * FROM plants WHERE id = :plantId")
    abstract PlantEntity getByIdRaw(int plantId);

    @Query("SELECT * FROM plants WHERE id IN (:plantIds)")
    abstract List<PlantEntity> getByIdRaw(List<Integer> plantIds);

    public LiveData<PlantEntity> getById(int plantId) {
        return Transformations.map(_loadById(plantId), PlantsMapper::map);
    }


    @Transaction
    @Query(plantsWithKeywordQuery + "ORDER BY LOWER(popularName)")
    abstract DataSource.Factory<Integer, FullPlantEntity> _loadAllWithKeyword(Boolean favorite, String keyword);

    public DataSource.Factory<Integer, PlantEntity> getAllWithKeyword(Boolean favorite, String keyword) {
        return _loadAllWithKeyword(favorite, keyword).map(PlantsMapper::map);
    }

    @Transaction
    @Query(plantsByQuery + "ORDER BY LOWER(popularName)")
    abstract DataSource.Factory<Integer, FullPlantEntity> _loadAllBy(String type,
                                                                     String species,
                                                                     String popularName,
                                                                     String naturalArea,
                                                                     String origin,
                                                                     String otherInfosTitle);

    public DataSource.Factory<Integer, PlantEntity> getAllBy(String type,
                                                             String species,
                                                             String popularName,
                                                             String naturalArea,
                                                             String origin,
                                                             String otherInfosTitle) {
        return _loadAllBy(type,
                species,
                popularName,
                naturalArea,
                origin,
                otherInfosTitle).map(PlantsMapper::map);
    }

    @Override
    @Transaction
    public void update(PlantEntity plantEntity) {
        PlantEntity oldPlantEntity = getByIdRaw(plantEntity.id);
        if (oldPlantEntity != null) {
            plantEntity.favorite = oldPlantEntity.favorite;
        }
        super.update(plantEntity);
    }

    @Override
    @Transaction
    public void update(List<PlantEntity> plantEntities) {

        List<PlantEntity> oldPlantEntities = getByIdRaw(plantEntities
                .stream()
                .map(p -> p.id)
                .collect(Collectors.toList()));
        plantEntities.forEach(plantEntity ->
                oldPlantEntities
                        .stream()
                        .filter(p -> p.id == plantEntity.id)
                        .findFirst()
                        .ifPresent(oldPlantEntity ->
                                plantEntity.favorite = oldPlantEntity.favorite));
        super.update(plantEntities);
    }

    @Transaction
    @Query("SELECT * FROM plants " +
            "WHERE typeId = :typeId AND id <> :plantId " +
            "ORDER BY LOWER(popularName)")
    abstract DataSource.Factory<Integer, FullPlantEntity> _loadPlantsFor(int plantId, int typeId);


    public DataSource.Factory<Integer, PlantEntity> getSimilarPlantsFor(int plantId, int typeId) {
        return _loadPlantsFor(plantId, typeId).map(PlantsMapper::map);
    }

    @Transaction
    @Query("SELECT * FROM plants " +
            "WHERE typeId = :typeId " +
            "ORDER BY id LIMIT :limit OFFSET :offset")
    abstract List<FullPlantEntity> _loadPlantsPageRawFor(int typeId, int offset, int limit);


    public List<PlantEntity> getPlantsPageRawFor(int typeId, int offset, int limit) {
        return _loadPlantsPageRawFor(typeId, offset, limit)
                .stream()
                .map(PlantsMapper::map)
                .collect(Collectors.toList());
    }

    @Query("UPDATE plants SET favorite = :favorite WHERE id = :plantId")
    public abstract void setFavorite(int plantId, boolean favorite);

    @Query("SELECT COUNT(id) FROM (" +
            plantsByQuery +
            ") WHERE LOWER(popularName) < LOWER(:targetPopularName)")
    public abstract int getPlantRowNumFor(String targetPopularName,
                                          String type,
                                          String species,
                                          String popularName,
                                          String naturalArea,
                                          String origin,
                                          String otherInfosTitle);

    @Query("SELECT COUNT(id) FROM (" +
            plantsWithKeywordQuery +
            ") WHERE LOWER(popularName) < LOWER(:targetPopularName)")
    public abstract int getPlantRowNumFor(String targetPopularName,
                                          Boolean favorite,
                                          String keyword);
}
