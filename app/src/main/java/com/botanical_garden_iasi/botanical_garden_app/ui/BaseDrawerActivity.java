package com.botanical_garden_iasi.botanical_garden_app.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.google.android.material.navigation.NavigationView;
import com.jaeger.library.StatusBarUtil;

public abstract class BaseDrawerActivity extends BaseActivity {

    protected NavigationView drawerNavigationView;
    protected DrawerLayout drawerLayout;
    protected AppBarConfiguration appBarConfiguration;
    protected NavController navController;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerNavigationView = findViewById(R.id.drawerNavView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        appBarConfiguration = getAppBarConfigurationBuilder()
                .setDrawerLayout(drawerLayout)
                .build();


        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(drawerNavigationView, navController);

        StatusBarUtil.setTranslucentForDrawerLayout(this, drawerLayout, 32);
    }

    public abstract AppBarConfiguration.Builder getAppBarConfigurationBuilder();

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavDestination currentDestination = navController.getCurrentDestination();
        if (currentDestination != null) {
            int currentDestinationId = currentDestination.getId();
            MenuItem currentDestinationMenuItem = drawerNavigationView.getMenu().findItem(currentDestinationId);
            currentDestinationMenuItem.setChecked(true);
        }
    }
}