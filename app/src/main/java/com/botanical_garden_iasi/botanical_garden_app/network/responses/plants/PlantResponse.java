package com.botanical_garden_iasi.botanical_garden_app.network.responses.plants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlantResponse {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("sectionId")
    @Expose
    public Integer sectionId;

    @SerializedName("ipen")
    @Expose
    public String ipen;

    @SerializedName("species")
    @Expose
    public SpeciesResponse species;

    @SerializedName("type")
    @Expose
    public TypeResponse type;

    @SerializedName("popularName")
    @Expose
    public String popularName;

    @SerializedName("scientificName")
    @Expose
    public String scientificName;

    @SerializedName("author")
    @Expose
    public String author;

    @SerializedName("origin")
    @Expose
    public String origin;

    @SerializedName("naturalArea")
    @Expose
    public String naturalArea;

    @SerializedName("otherInfos")
    @Expose
    public List<OtherPlantInfoResponse> otherInfo;

    @SerializedName("images")
    @Expose
    public List<PlantImageResponse> images;
}
