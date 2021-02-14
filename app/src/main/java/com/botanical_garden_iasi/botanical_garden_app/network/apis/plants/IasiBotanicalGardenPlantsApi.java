package com.botanical_garden_iasi.botanical_garden_app.network.apis.plants;

import androidx.lifecycle.LiveData;

import com.botanical_garden_iasi.botanical_garden_app.network.apis.ApiResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.plants.PlantResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface IasiBotanicalGardenPlantsApi {

    @GET("otherInfos/distinctTitles")
    LiveData<ApiResponse<List<String>>> getOtherPlantInfoDistinctTitles();

    @GET("plants/id/{plantId}")
    LiveData<ApiResponse<PlantResponse>> getPlant(@Path("plantId") int plantID);

    @GET("plants/by")
    Call<List<PlantResponse>> getPlantsRawFrom(@Query("typeId") int typesId,
                                               @Query("offset") int offset,
                                               @Query("limit") int limit);

    @GET("plants/by")
    Call<List<PlantResponse>> getPlantsRawBy(@Query("typeName") String type,
                                             @Query("speciesName") String species,
                                             @Query("popularName") String popularName,
                                             @Query("naturalArea") String naturalArea,
                                             @Query("origin") String origin,
                                             @Query("otherInfosTitle") String otherInfosTitle,
                                             @Query("offset") int offset,
                                             @Query("limit") int limit);

    @GET("plants/searchTextBy")
    Call<List<PlantResponse>> getPlantsRawWithKeyword(@Query("keyword") String keyword,
                                                      @Query("offset") int offset,
                                                      @Query("limit") int limit);
}
