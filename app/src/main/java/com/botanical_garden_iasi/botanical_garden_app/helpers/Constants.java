package com.botanical_garden_iasi.botanical_garden_app.helpers;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class Constants {

    public static final int VERTICAL_PAGE_SIZE = 10;
    public static final int HORIZONTAL_PAGE_SIZE = 5;
    private static long CACHE_TTL_MILIS;

    public static long getCacheTTLMilis() {
        return CACHE_TTL_MILIS;
    }

    public static void setCacheTTLMilis(long miliseconds) {
        CACHE_TTL_MILIS = miliseconds;
    }
}
