package com.botanical_garden_iasi.botanical_garden_app.network.responses.plants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TypeResponse {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("species")
    @Expose
    public List<SpeciesResponse> species;

    @SerializedName("plants")
    @Expose
    public List<PlantResponse> plants;
}
