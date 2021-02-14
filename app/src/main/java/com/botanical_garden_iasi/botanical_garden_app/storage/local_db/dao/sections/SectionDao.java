package com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.botanical_garden_iasi.botanical_garden_app.helpers.mappers.SectionsMapper;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.BaseDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.FullSectionEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Dao
public abstract class SectionDao extends BaseDao<SectionEntity> {

    private static final String sectionByQuery = "SELECT DISTINCT sections.* " +
            "FROM sections " +
            "WHERE LOWER(sections.name) LIKE '%' || LOWER(:name) || '%' ";

    @Transaction
    @Query("SELECT * FROM sections WHERE id = :sectionId")
    abstract LiveData<FullSectionEntity> _loadById(int sectionId);

    @Transaction
    @Query(sectionByQuery + "ORDER BY id LIMIT :limit OFFSET :offset")
    abstract List<FullSectionEntity> _loadSectionsPageRawBy(String name, int offset, int limit);

    @Transaction
    public List<SectionEntity> getSectionsPageRawBy(String name, int offset, int limit) {
        return _loadSectionsPageRawBy(name, offset, limit)
                .stream()
                .map(SectionsMapper::map)
                .collect(Collectors.toList());
    }


    @Query("SELECT * FROM sections WHERE id = :sectionId")
    abstract SectionEntity getByIdRaw(int sectionId);

    @Query("SELECT * FROM sections WHERE id IN (:sectionIds)")
    abstract List<SectionEntity> getByIdRaw(List<Integer> sectionIds);

    public LiveData<SectionEntity> getById(int sectionId) {
        return Transformations.map(_loadById(sectionId), SectionsMapper::map);
    }

    @Transaction
    @Query(sectionByQuery + "ORDER BY LOWER(name)")
    abstract DataSource.Factory<Integer, FullSectionEntity> _loadAllBy(String name);

    public DataSource.Factory<Integer, SectionEntity> getAllBy(String name) {
        return _loadAllBy(name).map(SectionsMapper::map);
    }

    @Query("SELECT COUNT(id) FROM (" +
            sectionByQuery +
            ") WHERE LOWER(name) < LOWER(:name)")
    public abstract int getPlantRowNumFor(String name);
}
