package com.botanical_garden_iasi.botanical_garden_app.ui.settings.user_settings_fragment;

import androidx.annotation.NonNull;

class CacheSpinnerItem {
    private String label;
    private long time;

    public CacheSpinnerItem(String label, long time) {
        this.label = label;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    @NonNull
    @Override
    public String toString() {
        return label;
    }
}
