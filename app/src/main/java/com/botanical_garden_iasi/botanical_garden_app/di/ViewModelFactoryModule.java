package com.botanical_garden_iasi.botanical_garden_app.di;

import androidx.lifecycle.ViewModelProvider;

import com.botanical_garden_iasi.botanical_garden_app.viewmodels.ViewModelProviderFactory;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Singleton
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);
}
