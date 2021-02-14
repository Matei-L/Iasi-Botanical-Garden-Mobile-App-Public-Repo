package com.botanical_garden_iasi.botanical_garden_app.di;

import com.botanical_garden_iasi.botanical_garden_app.di.games.GamesBuildersModule;
import com.botanical_garden_iasi.botanical_garden_app.di.games.GamesModule;
import com.botanical_garden_iasi.botanical_garden_app.di.games.GamesScope;
import com.botanical_garden_iasi.botanical_garden_app.di.games.GamesViewModelsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.MainBuildersModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.MainModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.MainScope;
import com.botanical_garden_iasi.botanical_garden_app.di.main.MainViewModelsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.plants.PlantsBuildersModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.plants.PlantsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.plants.PlantsScope;
import com.botanical_garden_iasi.botanical_garden_app.di.main.plants.PlantsViewModelsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.sections.SectionsBuildersModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.sections.SectionsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.sections.SectionsScope;
import com.botanical_garden_iasi.botanical_garden_app.di.main.sections.SectionsViewModelsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.settings.SettingsBuildersModule;
import com.botanical_garden_iasi.botanical_garden_app.di.settings.SettingsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.settings.SettingsScope;
import com.botanical_garden_iasi.botanical_garden_app.di.settings.SettingsViewModelsModule;
import com.botanical_garden_iasi.botanical_garden_app.ui.games.GameOneActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.MainActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plant_info_activity.PlantInfoActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.search.SearchActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.section_info_activity.SectionInfoActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.SettingsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class AppBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
            modules = {MainBuildersModule.class,
                    MainViewModelsModule.class,
                    MainModule.class
            }
    )
    abstract MainActivity contributeMainActivity();


    @MainScope
    @ContributesAndroidInjector(
            modules = {MainBuildersModule.class,
                    MainViewModelsModule.class,
                    MainModule.class
            }
    )
    abstract SearchActivity contributeSearchActivity();


    @SettingsScope
    @ContributesAndroidInjector(
            modules = {
                    SettingsBuildersModule.class,
                    SettingsViewModelsModule.class,
                    SettingsModule.class
            }
    )
    abstract SettingsActivity contributeSettingsActivity();

    @GamesScope
    @ContributesAndroidInjector(
            modules = {
                    GamesBuildersModule.class,
                    GamesViewModelsModule.class,
                    GamesModule.class
            }
    )
    abstract GameOneActivity contributeGamesActivity();


    @PlantsScope
    @ContributesAndroidInjector(
            modules = {
                    PlantsBuildersModule.class,
                    PlantsViewModelsModule.class,
                    PlantsModule.class
            }
    )
    abstract PlantInfoActivity contributePlantInfoActivity();

    @SectionsScope
    @ContributesAndroidInjector(
            modules = {
                    SectionsBuildersModule.class,
                    SectionsViewModelsModule.class,
                    SectionsModule.class
            }
    )
    abstract SectionInfoActivity contributeSectionInfoActivity();
}
