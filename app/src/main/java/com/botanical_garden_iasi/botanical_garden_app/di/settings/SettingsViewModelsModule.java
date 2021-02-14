package com.botanical_garden_iasi.botanical_garden_app.di.settings;

import androidx.lifecycle.ViewModel;

import com.botanical_garden_iasi.botanical_garden_app.di.ViewModelKey;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.games_settings_fragment.GamesSettingsViewModel;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.maps_settings_fragment.MapsSettingsViewModel;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.plants_settings_fragment.PlantsSettingsViewModel;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.user_settings_fragment.UserSettingsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SettingsViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(PlantsSettingsViewModel.class)
    public abstract ViewModel bindPlantsSettingsViewModel(PlantsSettingsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MapsSettingsViewModel.class)
    public abstract ViewModel bindMapsSettingsViewModel(MapsSettingsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GamesSettingsViewModel.class)
    public abstract ViewModel bindGamesSettingsViewModel(GamesSettingsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserSettingsViewModel.class)
    public abstract ViewModel bindUserSettingsViewModel(UserSettingsViewModel viewModel);
}
