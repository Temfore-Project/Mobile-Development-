package com.capstone.temfore.data.remote.retrofitimport com.capstone.temfore.BuildConfigimport okhttp3.OkHttpClientimport okhttp3.logging.HttpLoggingInterceptorimport retrofit2.Retrofitimport retrofit2.converter.gson.GsonConverterFactoryimport java.util.concurrent.TimeUnitclass ApiConfig {    companion object {        fun getApiService(): ApiService {            val loggingInterceptor =                if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)                else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)            val client = OkHttpClient.Builder()                .addInterceptor(loggingInterceptor)                .connectTimeout(120, TimeUnit.SECONDS) // Timeout koneksi                .readTimeout(120, TimeUnit.SECONDS)    // Timeout membaca response                .writeTimeout(120, TimeUnit.SECONDS)   // Timeout menulis data                .build()            val retrofit = Retrofit.Builder()                .baseUrl(BuildConfig.BASE_URL)                .addConverterFactory(GsonConverterFactory.create())                .client(client)                .build()            return retrofit.create(ApiService::class.java)        }    }}