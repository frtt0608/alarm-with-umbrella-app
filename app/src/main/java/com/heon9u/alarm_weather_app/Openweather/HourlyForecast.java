package com.heon9u.alarm_weather_app.Openweather;

import android.os.AsyncTask;
import android.util.Log;
import com.heon9u.alarm_weather_app.Dto.HourlyWeather;
import com.heon9u.alarm_weather_app.Dto.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HourlyForecast extends AsyncTask<String, Void, String> {

    public HourlyWeather[] hourlyWeathers;
    public boolean isFinish = false;

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
            return "open weather URL 접근 실패";
        }
    }

    public String downloadHourlyForecast(String urls) {
        HttpURLConnection conn = null;

        try {
            String hourlyUrl = urls + "&exclude=" + "current,minutely,daily,alerts";
            URL url = new URL(hourlyUrl);
            conn = (HttpURLConnection) url.openConnection();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();

                String line;
                while((line = br.readLine()) != null) {
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
                JSONArray jsonArray = jsonResult.getJSONArray("hourly");
                hourlyWeathers = new HourlyWeather[48];

                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject hourlyObject = jsonArray.getJSONObject(i);
                    HourlyWeather hourly = new HourlyWeather();

                    hourly.setDt(hourlyObject.optInt("dt"));
                    hourly.setTemp(hourlyObject.optDouble("temp"));
                    hourly.setFeels_like(hourlyObject.optDouble("feels_like"));
                    hourly.setPressure(hourlyObject.optInt("pressure"));
                    hourly.setHumidity(hourlyObject.optInt("humidity"));
                    hourly.setDew_point(hourlyObject.optDouble("dew_point"));
                    hourly.setUvi(hourlyObject.optDouble("uvi"));
                    hourly.setClouds(hourlyObject.optInt("clouds"));
                    hourly.setVisibility(hourlyObject.optInt("visibility"));
                    hourly.setWind_speed(hourlyObject.optDouble("wind_speed"));
                    hourly.setWind_gust(hourlyObject.optDouble("wind_gust"));
                    hourly.setWind_deg(hourlyObject.optInt("wind_deg"));
                    hourly.setPop(hourlyObject.optDouble("pop"));

                    JSONObject rainObject = hourlyObject.optJSONObject("rain");
                    JSONObject snowObject = hourlyObject.optJSONObject("snow");
                    if(rainObject != null) hourly.setRain1h(rainObject.optDouble("1h"));
                    if(snowObject != null) hourly.setRain1h(snowObject.optDouble("1h"));

                    JSONObject weatherObject = hourlyObject.getJSONArray("weather").getJSONObject(0);
                    Weather weather = new Weather();

                    weather.setId(weatherObject.optInt("id"));
                    weather.setMain(weatherObject.optString("main"));
                    weather.setDescription(weatherObject.optString("description"));
                    weather.setIcon(weatherObject.optString("icon"));

                    hourly.setWeather(weather);
                    hourly.changeUTCtoDate(hourly.getDt());

                    hourlyWeathers[i] = hourly;
                }

                isFinish = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}