package com.botanical_garden_iasi.botanical_garden_app.network.responses.sections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SectionResponse {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("locationAndSize")
    @Expose
    public String locationAndSize;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("latitude")
    @Expose
    public double latitude;

    @SerializedName("longitude")
    @Expose
    public double longitude;

//    @SerializedName("subsections")
    @SerializedName("subSections")
    @Expose
    public List<SubSectionResponse> subsections;

    @SerializedName("images")
    @Expose
    public List<SectionImageResponse> images;
}
