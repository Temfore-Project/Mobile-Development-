package com.capstone.temfore.data.remote.retrofitimport com.capstone.temfore.data.remote.response.ListRecommendItemimport com.capstone.temfore.data.remote.response.RecommendResponseimport com.capstone.temfore.data.remote.response.WeatherResponseimport retrofit2.Callimport retrofit2.http.*interface ApiService {    @GET("cuaca")    suspend fun getWeather(        @Query("lat") lat: Double,        @Query("lon") lon: Double,    ): WeatherResponse    @GET("recommend")    fun getRecommendations(        @Query("CategoryUser") categoryUser: String,        @Query("TempUser") tempUser: Int,        @Query("TimeUser") timeUser: Int    ): Call<RecommendResponse>    @GET("detail")    fun getFoodDetail(        @Query("id") foodId: Int    ): Call<ListRecommendItem>    @GET("cari")    fun searchFood(        @Query("title") query: String    ):Call<RecommendResponse>}