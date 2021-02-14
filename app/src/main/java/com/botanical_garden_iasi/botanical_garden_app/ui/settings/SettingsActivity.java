package com.botanical_garden_iasi.botanical_garden_app.ui.settings;

import android.os.Bundle;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.ui.BaseActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.plants_settings_fragment.PlantsSettingsFragment;
import com.botanical_garden_iasi.botanical_garden_app.ui.settings.user_settings_fragment.UserSettingsFragment;
import com.google.android.material.tabs.TabLayout;

public class SettingsActivity extends BaseActivity {
    private static final String TAG = "SettingsActivity";

    private SettingsPageAdapter settingsPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        setupTabLayout();
    }

    private void setupTabLayout() {
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getContentView() {
        return R.layout.settings_activity;
    }

    private void setupViewPager() {
        settingsPageAdapter = new SettingsPageAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        settingsPageAdapter.addFragment(new UserSettingsFragment(), getString(R.string.user_title));
        settingsPageAdapter.addFragment(new PlantsSettingsFragment(), getString(R.string.plants_title));
//        settingsPageAdapter.addFragment(new MapsSettingsFragment(), getString(R.string.maps_title));
//        settingsPageAdapter.addFragment(new GamesSettingsFragment(), getString(R.string.games_title));
        viewPager.setAdapter(settingsPageAdapter);
    }
}
