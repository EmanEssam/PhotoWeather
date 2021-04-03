package com.test.photoweather.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.photoweather.data.model.Weather
import com.test.photoweather.data.model.WeatherResponse
import com.test.photoweather.data.network.Api
import com.test.photoweather.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepo @Inject constructor(private val api: Api) {
    fun getWeather(lat: String, lng: String): LiveData<WeatherResponse> {
        val data = MutableLiveData<WeatherResponse>()
        val error = MutableLiveData<Boolean>()
        api.getWeatherByLocation(lat, lng, Constants.API_KEY)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {

                    data.value = response.body()
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

                    error.value = true
                }

            })
        return data

    }
}