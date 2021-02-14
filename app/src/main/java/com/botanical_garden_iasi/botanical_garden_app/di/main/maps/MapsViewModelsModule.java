package com.botanical_garden_iasi.botanical_garden_app.di.main.maps;

import androidx.lifecycle.ViewModel;

import com.botanical_garden_iasi.botanical_garden_app.di.ViewModelKey;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.maps.MapsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MapsViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapsViewModel.class)
    public abstract ViewModel bindPlantsMainMapsViewModel(MapsViewModel viewModel);
}
