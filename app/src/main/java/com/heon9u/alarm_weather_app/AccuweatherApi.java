package com.heon9u.alarm_weather_app;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccuweatherApi extends AsyncTask<String, Void, String> {
    private final String serviceKey = "AZPtVq7G1R652UPuvauPNNoBBXbgxZCz";
    private final String locationsUrl = "http://dataservice.accuweather.com/locations/v1/cities/search";
    private final String forecastsUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/";
    String locationKey = "";

    @Override
    protected String doInBackground(String... location) {
        try {
            return searchLocationKey((String) location[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "Access accuweatherApi failed";
        }
    }

    public String searchLocationKey(String location) {

        HttpURLConnection conn = null;

        try {
            String strUrl = locationsUrl + "?apikey=" + serviceKey + "&q=" + location;
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();

            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            conn.disconnect();
        }
    }
}

