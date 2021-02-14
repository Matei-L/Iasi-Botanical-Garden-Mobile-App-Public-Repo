package com.botanical_garden_iasi.botanical_garden_app.di;

import android.app.Application;

import com.botanical_garden_iasi.botanical_garden_app.IasiBotanicalGardenApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppBuildersModule.class,
        AppModule.class,
        ViewModelFactoryModule.class
})
public interface IasiBotanicalGardenAppComponent
        extends AndroidInjector<IasiBotanicalGardenApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        IasiBotanicalGardenAppComponent build();
    }
}