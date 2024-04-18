package com.example.amphibians.network

import com.example.amphibians.model.AmphibiansPhoto
import retrofit2.http.GET

interface AmphibiansApiService {
    @GET("photos")
    suspend fun getPhotos(): List<AmphibiansPhoto>
}
