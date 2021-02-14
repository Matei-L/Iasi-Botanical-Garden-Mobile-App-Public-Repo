package com.botanical_garden_iasi.botanical_garden_app.ui.games;

import android.os.Bundle;

import com.botanical_garden_iasi.botanical_garden_app.R;
import com.botanical_garden_iasi.botanical_garden_app.ui.BaseActivity;

public class GameOneActivity extends BaseActivity {
    private static final String TAG = "GameOneActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.game_one_activity;
    }
}
