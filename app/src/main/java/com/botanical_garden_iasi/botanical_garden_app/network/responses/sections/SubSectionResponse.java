package com.botanical_garden_iasi.botanical_garden_app.network.responses.sections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubSectionResponse {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("description")
    @Expose
    public String description;
}
