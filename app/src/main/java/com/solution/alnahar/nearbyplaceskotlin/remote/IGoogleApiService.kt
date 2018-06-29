package com.solution.alnahar.nearbyplaceskotlin.remote

import com.solution.alnahar.nearbyplaceskotlin.model.MyPlaces
import com.solution.alnahar.nearbyplaceskotlin.model.PlaceDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface IGoogleApiService {

    @GET
    fun getNearByPlaces(@Url url: String): Call<MyPlaces>

    @GET
    fun getPlaceDetail(@Url url: String): Call<PlaceDetail>
}