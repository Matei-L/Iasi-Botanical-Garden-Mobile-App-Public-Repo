package com.botanical_garden_iasi.botanical_garden_app.di.main.plants;

import androidx.lifecycle.ViewModel;

import com.botanical_garden_iasi.botanical_garden_app.di.ViewModelKey;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plant_info_activity.PlantInfoViewModel;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plants_list_fragment.PlantsListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class PlantsViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlantInfoViewModel.class)
    public abstract ViewModel bindPlantInformationViewModel(PlantInfoViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlantsListViewModel.class)
    public abstract ViewModel bindPlantsListViewModel(PlantsListViewModel viewModel);
}
