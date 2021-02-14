package com.botanical_garden_iasi.botanical_garden_app.network.responses.plants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherPlantInfoResponse {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("description")
    @Expose
    public String description;
}
