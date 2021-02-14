package com.botanical_garden_iasi.botanical_garden_app;

import android.content.Context;
import android.content.SharedPreferences;

import com.botanical_garden_iasi.botanical_garden_app.di.DaggerIasiBotanicalGardenAppComponent;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;

import java.util.concurrent.TimeUnit;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class IasiBotanicalGardenApp extends DaggerApplication {

    private SharedPreferences cachingSharedPref;

    @Override
    public void onCreate() {
        super.onCreate();
        lookupPreferences();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerIasiBotanicalGardenAppComponent.builder().application(this).build();
    }

    private void lookupPreferences() {
        SharedPreferences cachingSharedPref = getApplicationContext()
                .getSharedPreferences("Caching", Context.MODE_PRIVATE);
        long cachingTTL = cachingSharedPref.getLong(getString(R.string.caching_ttl), 0);
        if (cachingTTL > 0) {
            Constants.setCacheTTLMilis(cachingTTL);
        } else {
            Constants.setCacheTTLMilis(TimeUnit.MINUTES.toMillis(10));
        }
    }


    @Override
    public void onTerminate() {
        savePreferances();
        super.onTerminate();
    }

    private void savePreferances() {
        SharedPreferences.Editor editor = cachingSharedPref.edit();
        editor.putLong(getString(R.string.caching_ttl), Constants.getCacheTTLMilis());
        editor.apply();
    }
}
