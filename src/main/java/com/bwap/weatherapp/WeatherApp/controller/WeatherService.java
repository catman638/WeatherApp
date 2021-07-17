package com.bwap.weatherapp.WeatherApp.controller;

//import elemental.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {

    private OkHttpClient client;
    private Response response;
    private String CityName;
    String unit;
    private String API="c847190b05fbae98550b042c08299b75";


    public JSONObject getWeather(){
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnit()+ "&appid=c847190b05fbae98550b042c08299b75")
                .build();

        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray returnWeatherArray() throws JSONException {
        JSONArray weatherArray = getWeather().getJSONArray("weather");
        return weatherArray;
    }

    public JSONObject returnMain() throws JSONException {
        JSONObject main = getWeather().getJSONObject("main");
        return main;
    }
    public JSONObject returnWind() throws JSONException {
        JSONObject wind = getWeather().getJSONObject("wind");
        return wind;
    }
    public JSONObject returnSys() throws JSONException {
        JSONObject sys = getWeather().getJSONObject("sys");
        return sys;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
