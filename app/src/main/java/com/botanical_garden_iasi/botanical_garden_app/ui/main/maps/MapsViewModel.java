package com.botanical_garden_iasi.botanical_garden_app.ui.main.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.botanical_garden_iasi.botanical_garden_app.repositories.sections.SectionRepository;

import javax.inject.Inject;

public class MapsViewModel extends ViewModel {

    private SectionRepository sectionRepository;


    @Inject
    MapsViewModel(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    LiveData<PagedList<Section>> getSections() {
        return sectionRepository.getAllSections(false);
    }


}
