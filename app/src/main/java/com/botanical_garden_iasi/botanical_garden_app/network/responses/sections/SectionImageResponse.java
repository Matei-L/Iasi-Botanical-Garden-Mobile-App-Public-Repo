package com.botanical_garden_iasi.botanical_garden_app.network.responses.sections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionImageResponse {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("url")
    @Expose
    public String url;
}
