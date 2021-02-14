package com.botanical_garden_iasi.botanical_garden_app.di;

import android.app.Application;

import androidx.room.Room;

import com.botanical_garden_iasi.botanical_garden_app.AppExecutors;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.LiveDataCallAdapter;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.plants.IasiBotanicalGardenPlantsApi;
import com.botanical_garden_iasi.botanical_garden_app.network.apis.sections.IasiBotanicalGardenSectionsApi;
import com.botanical_garden_iasi.botanical_garden_app.storage.local_db.IasiBotanicalGardenDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class AppModule {
    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.IASI_BOTANICAL_GARDEN_API_URL)
                .addCallAdapterFactory(LiveDataCallAdapter.Factory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static IasiBotanicalGardenDatabase provideIasiBotanicalGardenDatabaseInstance(Application application) {
        return Room.databaseBuilder(
                application,
                IasiBotanicalGardenDatabase.class,
                "IasiBotanicalGarden.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    static AppExecutors provideAppExecutorsInstance() {
        return new AppExecutors();
    }

    @Singleton
    @Provides
    static IasiBotanicalGardenPlantsApi provideIasiBotanicalGardenPlantsApiInstance(Retrofit retrofit) {
        return retrofit.create(IasiBotanicalGardenPlantsApi.class);
    }

    @Singleton
    @Provides
    static IasiBotanicalGardenSectionsApi provideIasiBotanicalGardenSectionsApiInstance(Retrofit retrofit) {
//        return new Retrofit.Builder()
//                .baseUrl("https://iasi-botanical-garden-app.appspot.com/")
//                .addCallAdapterFactory(LiveDataCallAdapter.Factory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(IasiBotanicalGardenSectionsApi.class);
        return retrofit.create(IasiBotanicalGardenSectionsApi.class);
    }
}
