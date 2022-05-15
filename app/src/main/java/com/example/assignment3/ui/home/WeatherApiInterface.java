package com.example.assignment3.ui.home;

import com.example.assignment3.R;

import retrofit2.Call;
import retrofit2.http.GET;

//version 1.0.1: weatherAPI interface --Hongyu
//  https://api.openweathermap.org/data/2.5/weather?lat=-37.840935&lon=144.946457&appid=0891ee5dc748292618ab5b5fc73361bf


public interface WeatherApiInterface {
 //   String apiLink = "weather?lat=-37.840935&lon=144.946457&appid=" + R.string.open_weather_API_key;
    @GET("weather?lat=-37.840935&lon=144.946457&appid=0891ee5dc748292618ab5b5fc73361bf")
    Call<Root> getWeather();
}
