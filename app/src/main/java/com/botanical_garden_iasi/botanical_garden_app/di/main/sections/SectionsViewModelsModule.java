package com.botanical_garden_iasi.botanical_garden_app.di.main.sections;

import androidx.lifecycle.ViewModel;

import com.botanical_garden_iasi.botanical_garden_app.di.ViewModelKey;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.section_info_activity.SectionInfoViewModel;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.sections_list_fragment.SectionsListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SectionsViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SectionsListViewModel.class)
    public abstract ViewModel bindSectionsListViewModel(SectionsListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SectionInfoViewModel.class)
    public abstract ViewModel bindSectionInfoViewModel(SectionInfoViewModel viewModel);
}
