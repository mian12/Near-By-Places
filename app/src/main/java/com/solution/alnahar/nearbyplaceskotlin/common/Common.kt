package com.solution.alnahar.nearbyplaceskotlin.common

import com.solution.alnahar.nearbyplaceskotlin.model.Results
import com.solution.alnahar.nearbyplaceskotlin.remote.IGoogleApiService
import com.solution.alnahar.nearbyplaceskotlin.remote.RetrofitClient

object Common {


    private  var  GOOGLE_API_URL="https://maps.googleapis.com/"

    var  currentResult:Results?=null

    val googleApiService:IGoogleApiService
    get() = RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleApiService::class.java)

}