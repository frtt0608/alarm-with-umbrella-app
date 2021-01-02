package com.heon9u.alarm_weather_app;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HourlyForecast extends AsyncTask<String, Void, String> {
    protected final String openweatherUrl = "https://api.openweathermap.org/data/2.5/onecall";
    protected final String apiKey = "6e20ff161911d310524f6a26ac649500";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
           return downloadHourlyForecast(urls[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "hourly forecast URL 접근 실패";
        }
    }

    public String downloadHourlyForecast(String temp) {
        HttpURLConnection conn = null;
//        37.45746122172504, 126.72263584810149
        try {
            String lat = "37.45746122172504";
            String lon = "126.72263584810149";
            String hourlyUrl = openweatherUrl + "?lat=" + lat + "&lon=" + lon + "&exclude=" + "current, minutely, daily, alerts" +
                                "&appid=" + apiKey;
            URL url = new URL(hourlyUrl);
            conn = (HttpURLConnection) url.openConnection();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
                BufferedReader in = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();

                String line;
                while((line = in.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();

            } else {
                System.out.println("hourly forecast HTTP failed");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String apiResult) {
        if(apiResult != null) {
            try {
                JSONObject jsonResult = new JSONObject(apiResult);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
