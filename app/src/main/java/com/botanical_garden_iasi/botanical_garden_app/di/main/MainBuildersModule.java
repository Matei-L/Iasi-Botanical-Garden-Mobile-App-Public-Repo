package com.botanical_garden_iasi.botanical_garden_app.di.main;

import com.botanical_garden_iasi.botanical_garden_app.di.main.maps.MapsBuildersModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.maps.MapsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.maps.MapsScope;
import com.botanical_garden_iasi.botanical_garden_app.di.main.maps.MapsViewModelsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.plants.PlantsBuildersModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.plants.PlantsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.plants.PlantsScope;
import com.botanical_garden_iasi.botanical_garden_app.di.main.plants.PlantsViewModelsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.qr.QrScope;
import com.botanical_garden_iasi.botanical_garden_app.di.main.sections.SectionsBuildersModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.sections.SectionsModule;
import com.botanical_garden_iasi.botanical_garden_app.di.main.sections.SectionsScope;
import com.botanical_garden_iasi.botanical_garden_app.di.main.sections.SectionsViewModelsModule;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.maps.MapsFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.plants.plants_list_fragment.PlantsListFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.qr.QRScannerFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.sections.sections_list_fragment.SectionsListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainBuildersModule {
    @MapsScope
    @ContributesAndroidInjector(
            modules = {
                    MapsBuildersModule.class,
                    MapsViewModelsModule.class,
                    MapsModule.class
            }
    )
    abstract MapsFragment contributeMainMapsFragment();


    @PlantsScope
    @ContributesAndroidInjector(
            modules = {
                    PlantsBuildersModule.class,
                    PlantsViewModelsModule.class,
                    PlantsModule.class
            }
    )
    abstract PlantsListFragment contributePlantsListFragment();

    @QrScope
    @ContributesAndroidInjector()
    abstract QRScannerFragment contributeQRScannerFragment();

    @SectionsScope
    @ContributesAndroidInjector(
            modules = {
                    SectionsBuildersModule.class,
                    SectionsViewModelsModule.class,
                    SectionsModule.class
            }
    )
    abstract SectionsListFragment contributeSectionsListFragment();
}
