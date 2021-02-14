package com.botanical_garden_iasi.botanical_garden_app.ui.main;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.widget.SearchView;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.helpers.Constants;
import com.botanical_garden_iasi.botanical_garden_app.ui.BaseDrawerActivity;
import com.botanical_garden_iasi.botanical_garden_app.ui.main.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseDrawerActivity {
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        redirectToSection();
    }

    private void redirectToSection() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int sectionId = bundle.getInt("sectionId");
            if (sectionId > 0) {
                navController.navigate(R.id.action_global_navigation_maps);
            }
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.main_activity;
    }

    @Override
    public AppBarConfiguration.Builder getAppBarConfigurationBuilder() {
        return new AppBarConfiguration.Builder(
                R.id.navigation_qr,
                R.id.navigation_plants_list,
                R.id.navigation_settings_activity,
                R.id.navigation_games_activity,
                R.id.navigation_maps
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);

        setupSearchView(menu);

        return true;
    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getBaseContext(), SearchActivity.class)));

        searchView.onActionViewExpanded();
        makeSearchViewBehaveLikeAButton(searchView);
    }

    private void makeSearchViewBehaveLikeAButton(SearchView searchView) {
        searchView.clearFocus();
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchView.setQuery(Constants.SEARCH_HOME_KEYWORD, true);
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        });
    }
}
