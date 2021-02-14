package com.botanical_garden_iasi.botanical_garden_app.di.settings;

import com.botanical_garden_iasi.botanical_garden_app.ui.settings.games_settings_fragment.GamesSettingsFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.maps_settings_fragment.MapsSettingsFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.plants_settings_fragment.PlantsSettingsFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.user_settings_fragment.UserSettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingsBuildersModule {
    @ContributesAndroidInjector()
    abstract PlantsSettingsFragment contributePlantsSettingsFragment();

    @ContributesAndroidInjector()
    abstract MapsSettingsFragment contributeMapsSettingsFragment();

    @ContributesAndroidInjector()
    abstract GamesSettingsFragment contributeGamesSettingsFragment();

    @ContributesAndroidInjector()
    abstract UserSettingsFragment contributeUserSettingsFragment();

}
