package com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.section_info_activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.botanical_garden_iasi.botanical_garden_app.network.Resource;
import com.botanical_garden_iasi.botanical_garden_app.repositories.models.sections.Section;
import com.botanical_garden_iasi.botanical_garden_app.repositories.sections.SectionRepository;

import javax.inject.Inject;

public class SectionInfoViewModel extends ViewModel {
    private static final String TAG = "SectionInfoViewModel";

    private SectionRepository sectionRepository;

    private LiveData<Resource<Section>> sectionResource = null;
    private int sectionId;

    @Inject
    SectionInfoViewModel(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getSectionId() {
        return sectionId;
    }

    LiveData<Resource<Section>> getSectionResource() {
        if (sectionResource == null) {
            sectionResource = sectionRepository.getSection(sectionId);
        }
        return sectionResource;
    }
}
