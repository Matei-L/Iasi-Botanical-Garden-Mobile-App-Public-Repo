package com.botanical_garden_iasi.botanical_garden_app.repositories.sections;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;
import com.botanical_garden_iasi.botanical_garden_app.helpers.mappers.SectionsMapper;
import com.botanical_garden_iasi.botanical_garden_app.network.BoundaryCallback;
import com.botanical_garden_iasi.botanical_garden_app.network.NetworkBoundResource;
import com.botanical_garden_iasi.botanical_garden_app.network.Resource;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.ApiResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.sections.IasiBotanicalGardenSectionsApi;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.sections.SectionResponse;
import com.botanical_garden_iasi.botanical_garden_app.repositories.Repository;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SectionImageDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.dao.sections.SubSectionDao;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SectionImageEntity;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.entities.sections.SubSectionEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import javax.inject.Inject;

import retrofit2.Call;

public class SectionRepository extends Repository {
    private static final String TAG = "SectionRepository";
    private IasiBotanicalGardenSectionsApi api;
    private SectionDao sectionDao;
    private SubSectionDao subsectionDao;
    private SectionImageDao sectionImageDao;

    @Inject
    public SectionRepository(IasiBotanicalGardenSectionsApi api,
                             SectionDao sectionDao,
                             SubSectionDao subsectionDao,
                             SectionImageDao sectionImageDao, AppExecutors executors) {
        this.api = api;
        this.executors = executors;
        this.sectionDao = sectionDao;
        this.subsectionDao = subsectionDao;
        this.sectionImageDao = sectionImageDao;
    }

    public LiveData<Resource<Section>> getSection(int sectionID) {
        return new NetworkBoundResource<Section, SectionResponse>(executors) {
            @Override
            protected void onFetchFailed() {
                Log.e(TAG, "Fetching the section wit id " + sectionID + " failed.");
            }

            @Override
            protected void saveCallResult(SectionResponse sectionResponse) {
                if (sectionResponse != null) {

                    SectionEntity sectionEntity = SectionsMapper.map(sectionResponse);

                    upsert(sectionEntity);

                    Log.i(TAG, "Saved the section with id " + sectionID + " into our local database.");
                }
            }

            @Override
            protected boolean shouldFetch(Section section) {
                boolean fetch = SectionRepository.this.shouldFetch(section);
                Log.i(TAG, "shouldFetch section " + (section != null ? section.id : "<<new section>>") + ": " + fetch);
                return fetch;
            }

            @Override
            protected LiveData<Section> loadFromDb() {
                return Transformations.map(sectionDao.getById(sectionID), SectionsMapper::map);
            }

            @Override
            protected LiveData<ApiResponse<SectionResponse>> createCall() {
                LiveData<ApiResponse<SectionResponse>> call = api.getSection(sectionID);
                Log.i(TAG, "Called the api for info related to the section with id " + sectionID);
                return call;
            }
        }.asLiveData();
    }

    public LiveData<PagedList<Section>> getAllSections(boolean forceFetch) {
        return getSections(forceFetch, "", 0);
    }

    public LiveData<PagedList<Section>> getSections(boolean forceFetch, String keyword, int optionIndex) {

        Log.i(TAG, "getPlants: keyword: " + keyword + " optionIndex " + optionIndex);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(Constants.VERTICAL_PAGE_SIZE / 2)
                .setPageSize(Constants.VERTICAL_PAGE_SIZE)
                .setPrefetchDistance(Constants.VERTICAL_PAGE_SIZE / 2)
                .build();

        Section.FilterOptions option = Section.FilterOptions.values()[optionIndex];
        DataSource.Factory<Integer, SectionEntity> datasource;
        datasource = getDataSourceForFilter(keyword, option);

        return new LivePagedListBuilder<>(datasource.map(SectionsMapper::map), config)
                .setBoundaryCallback(new BoundaryCallback<SectionResponse, Section>(Constants.VERTICAL_PAGE_SIZE) {
                    @Override
                    public Executor getExecutor() {
                        return executors.io();
                    }

                    @Override
                    public Call<List<SectionResponse>> createCall(int offset, int limit) {
                        Log.i(TAG, "Sections BoundaryCallback createCall: offset(" + offset + "), limit(" + limit + ").");
                        return getAPICallForFilter(keyword, option, offset, limit);
                    }

                    @Override
                    public void saveResponses(List<SectionResponse> sectionResponses, int offset, int limit) {
                        if (sectionResponses != null && sectionResponses.size() > 0) {
                            List<SectionEntity> sectionEntities = sectionResponses
                                    .stream()
                                    .map(SectionsMapper::map)
                                    .collect(Collectors.toList());

                            List<Integer> sectionEntityIds = sectionEntities
                                    .stream()
                                    .map(s -> s.id)
                                    .collect(Collectors.toList());

                            List<SectionEntity> sectionEntitiesToDelete =
                                    getSectionsPageRaw(keyword, option, offset, limit)
                                            .stream()
                                            .filter(otd -> !sectionEntityIds.contains(otd.id))
                                            .collect(Collectors.toList());
                            Log.i(TAG, "upsert: " + sectionEntitiesToDelete.size() +
                                    " sections to delete for offset "
                                    + offset + " limit "
                                    + limit);

                            sectionDao.delete(sectionEntitiesToDelete);

                            upsert(sectionEntities);

                            Log.i(TAG, "Saved all " +
                                    sectionEntities.size() + " sections for offset(" +
                                    offset + ") and limit(" + limit +
                                    ") into our local database.");
                        }
                    }

                    @Override
                    public boolean shouldFetch(List<Section> items) {
                        boolean fetch = SectionRepository.this.shouldFetch(items);
                        Log.i(TAG, "sections shouldFetch: " + fetch + " forceFetch: " + forceFetch);
                        return fetch || forceFetch;
                    }

                    @Override
                    protected List<Section> loadPageRawFromDb(int offset, int limit) {
                        return getSectionsPageRaw(keyword, option, offset, limit)
                                .stream()
                                .map(SectionsMapper::map)
                                .collect(Collectors.toList());
                    }

                    @Override
                    protected int getPage(Section section, int limit) {
                        Log.i(TAG, "getPage: getting page for " + section.id);
                        return getSectionRowNumForWithFilter(section.name, option) / limit;
                    }
                }).build();
    }

    private void upsert(SectionEntity sectionEntity) {
        sectionDao.upsert(sectionEntity);

        List<SubSectionEntity> subsectionEntities = sectionEntity.subsections;

        List<Integer> subsectionEntityIds = subsectionEntities
                .stream()
                .map(s -> s.id)
                .collect(Collectors.toList());
        List<SubSectionEntity> subsectionEntitiesToDelete =
                subsectionDao.getAllSubsectionsRawFor(sectionEntity.id)
                        .stream()
                        .filter(otd -> !subsectionEntityIds.contains(otd.id))
                        .collect(Collectors.toList());

        subsectionDao.delete(subsectionEntitiesToDelete);

        subsectionDao.upsert(subsectionEntities);

        List<SectionImageEntity> sectionImageEntities = sectionEntity.images;
        List<Integer> plantImageEntityIds = sectionImageEntities
                .stream()
                .map(i -> i.id)
                .collect(Collectors.toList());
        List<SectionImageEntity> sectionImageEntitiesToDelete =
                sectionImageDao.getAllImagesRawFor(sectionEntity.id)
                        .stream()
                        .filter(itd -> !plantImageEntityIds.contains(itd.id))
                        .collect(Collectors.toList());

        sectionImageDao.delete(sectionImageEntitiesToDelete);
        sectionImageDao.upsert(sectionImageEntities);
    }

    private void upsert(List<SectionEntity> sectionEntities) {

        sectionDao.upsert(sectionEntities);

        List<Integer> plantEntityIds = sectionEntities
                .stream()
                .map(p -> p.id)
                .collect(Collectors.toList());

        List<SubSectionEntity> subsectionEntities = sectionEntities
                .stream()
                .flatMap(p -> p.subsections.stream())
                .collect(Collectors.toList());

        List<SectionImageEntity> sectionImageEntities = sectionEntities
                .stream()
                .flatMap(p -> p.images.stream())
                .collect(Collectors.toList());

        List<Integer> otherPlantInfoEntityIds = subsectionEntities
                .stream()
                .map(o -> o.id)
                .collect(Collectors.toList());
        List<SubSectionEntity> subsectionEntitiesToDelete =
                subsectionDao.getAllSubsectionsRawFor(plantEntityIds)
                        .stream()
                        .filter(otd -> !otherPlantInfoEntityIds.contains(otd.id))
                        .collect(Collectors.toList());

        subsectionDao.delete(subsectionEntitiesToDelete);

        subsectionDao.upsert(subsectionEntities);


        List<Integer> plantImageEntityIds = sectionImageEntities
                .stream()
                .map(o -> o.id)
                .collect(Collectors.toList());
        List<SectionImageEntity> sectionImageEntitiesToDelete =
                sectionImageDao.getAllImagesRawFor(plantEntityIds)
                        .stream()
                        .filter(itd -> !plantImageEntityIds.contains(itd.id))
                        .collect(Collectors.toList());

        sectionImageDao.delete(sectionImageEntitiesToDelete);
        sectionImageDao.upsert(sectionImageEntities);
    }

    private DataSource.Factory<Integer, SectionEntity> getDataSourceForFilter(String keyword, Section.FilterOptions option) {
        DataSource.Factory<Integer, SectionEntity> datasource;
        if (option == Section.FilterOptions.NAME) {
            datasource = sectionDao.getAllBy(keyword);
        } else {
            datasource = sectionDao.getAllBy("");
        }
        return datasource;
    }


    private Call<List<SectionResponse>> getAPICallForFilter(String keyword, Section.FilterOptions option,
                                                            int offset, int limit) {
        Call<List<SectionResponse>> call;
        if (option == Section.FilterOptions.NAME) {
            if (keyword != "") {
                call = api.getSectionsRawByName(keyword, offset, limit);
            } else {
                Log.d(TAG, "getAPICallForFilter: no keyword");
                call = api.getSectionsRaw(offset, limit);
            }
        } else {
            call = api.getSectionsRaw(offset, limit);
        }
        return call;
    }

    private List<SectionEntity> getSectionsPageRaw(String keyword, Section.FilterOptions option,
                                                   int offset, int limit) {
        List<SectionEntity> sectionsPageRaw;
        if (option == Section.FilterOptions.NAME) {
            sectionsPageRaw = sectionDao.getSectionsPageRawBy(keyword, offset, limit);
        } else {
            sectionsPageRaw = sectionDao.getSectionsPageRawBy("", offset, limit);
        }

        Log.i(TAG, "getSectionsPageRaw: pagesize: " + sectionsPageRaw.size());

        return sectionsPageRaw;
    }

    private int getSectionRowNumForWithFilter(String name, Section.FilterOptions option) {
        int rowNum;
        rowNum = sectionDao.getPlantRowNumFor(name);

        return rowNum;
    }
}
