package com.botanical_garden_iasi.botanical_garden_app.network.apis.sections;

import androidx.lifecycle.LiveData;

import com.botanical_garden_iasi.botanical_garden_app.network.apis.ApiResponse;
import com.botanical_garden_iasi.botanical_garden_app.network.responses.sections.SectionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IasiBotanicalGardenSectionsApi {

    //    @GET("/sections/id/{sectionId}")
    @GET("section/id/{sectionId}")
    LiveData<ApiResponse<SectionResponse>> getSection(@Path("sectionId") int sectionID);


    //    @GET("sections")
    @GET("section/searchTextBy")
    Call<List<SectionResponse>> getSectionsRawByName(@Query("sectionName") String sectionName,
                                                     @Query("offset") int offset,
                                                     @Query("limit") int limit);

    @GET("section")
    Call<List<SectionResponse>> getSectionsRaw(@Query("offset") int offset,
                                               @Query("limit") int limit);

}
