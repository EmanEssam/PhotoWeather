package com.test.photoweather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.photoweather.data.model.Weather
import com.test.photoweather.data.model.WeatherResponse
import com.test.photoweather.data.repo.MainRepo
import com.test.photoweather.utils.observeOnce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject
import kotlin.math.ln

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: MainRepo) : ViewModel() {
    val weather: MutableLiveData<WeatherResponse> by lazy {
        MutableLiveData<WeatherResponse>()
    }

    fun getWeather(lat: String, lng: String): MutableLiveData<WeatherResponse> {

        repo.getWeather(lat, lng).observeOnce {

            weather.postValue(it)
        }

        return weather
    }


}