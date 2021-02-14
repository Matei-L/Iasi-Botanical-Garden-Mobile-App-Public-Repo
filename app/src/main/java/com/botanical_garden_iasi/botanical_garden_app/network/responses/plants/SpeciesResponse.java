package com.botanical_garden_iasi.botanical_garden_app.network.responses.plants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpeciesResponse {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("type")
    @Expose
    public TypeResponse type;

    @SerializedName("plants")
    @Expose
    public List<PlantResponse> plants;
}
