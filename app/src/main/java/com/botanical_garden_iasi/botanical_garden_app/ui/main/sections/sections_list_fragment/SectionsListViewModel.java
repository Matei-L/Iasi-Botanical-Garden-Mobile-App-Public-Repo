package com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.sections_list_fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.botanical_garden_iasi.botanical_garden_app.repositories.sections.SectionRepository;

import javax.inject.Inject;

public class SectionsListViewModel extends ViewModel {
    private static final String TAG = "SectionsListViewModel";

    @Inject
    SectionRepository sectionRepository;
    private LiveData<PagedList<Section>> sections;
    private int currentOption;
    private String currentKeyword;

    @Inject
    SectionsListViewModel(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
        sections = null;
        clearFilters();
    }

    public void setFilters(String keyword, int option) {
        currentOption = option;
        currentKeyword = keyword;
    }

    public void clearFilters() {
        currentOption = 0;
        currentKeyword = "";
    }

    public int getCurrentOption() {
        return currentOption;
    }

    public String getCurrentKeyword() {
        return currentKeyword;
    }

    LiveData<PagedList<Section>> getSections() {
        if (sections != null) {
            return sections;
        }
        return getSections(false);
    }

    LiveData<PagedList<Section>> getSections(boolean forceRefresh) {
        sections = sectionRepository.getSections(forceRefresh, currentKeyword, currentOption);
        return sections;
    }
}
