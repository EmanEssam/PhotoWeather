package com.test.photoweather.data.network

import com.test.photoweather.data.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {


    @GET("weather")
    fun getWeatherByLocation(
        @Query("lat") latitude: String, @Query("lon") longitude: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>


    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}
