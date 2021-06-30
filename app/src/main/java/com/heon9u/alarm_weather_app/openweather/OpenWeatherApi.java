package com.heon9u.alarm_weather_app.openweather;

import android.os.AsyncTask;

import com.heon9u.alarm_weather_app.dto.CurrentWeather;
import com.heon9u.alarm_weather_app.dto.DailyWeather;
import com.heon9u.alarm_weather_app.dto.Feels_like;
import com.heon9u.alarm_weather_app.dto.HourlyWeather;
import com.heon9u.alarm_weather_app.dto.Temperature;
import com.heon9u.alarm_weather_app.dto.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class OpenWeatherApi extends AsyncTask<String, Void, String> {

    private final static String openWeatherUrl = "https://api.openweathermap.org/data/2.5/onecall";
    private final static String apiKey = "6e20ff161911d310524f6a26ac649500";
    public CurrentWeather currentWeather;
    public HourlyWeather[] hourlyWeathers;
    public DailyWeather[] dailyWeathers;

    final int HOURLY = 48;
    final int CURRENT = 1;
    final int DAILY = 7;
    final int PREVIOUS = 5;

    public int apiType;

    public OpenWeatherApi(int apiType) {
        this.apiType = apiType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadWeatherForecast(urls[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "open weather URL 접근 실패";
        }
    }

    public String setWeatherUrl(String urls) {
        urls = openWeatherUrl + urls + "&appid=" + apiKey;

        switch (apiType) {
            case CURRENT:
                return urls + "&exclude=minutely,hourly,daily,alerts";
            case HOURLY:
                return urls + "&exclude=current,minutely,daily,alerts";
            case DAILY:
                return urls + "&exclude=current,minutely,hourly,alerts";
            case PREVIOUS:
                return urls + "&dt = time";
            default:
                return urls + "&exclude=alerts";
        }
    }

    @Override
    protected void onPostExecute(String apiResult) {
        if(apiResult != null) {
            switch (apiType) {
                case CURRENT:
                    parsingCurrentWeather(apiResult);
                    break;
                case HOURLY:
                    parsingHourlyWeather(apiResult);
                    break;
                case DAILY:
                    parsingDailyWeather(apiResult);
                    break;
                default:
                    parsingCurrentWeather(apiResult);
                    parsingHourlyWeather(apiResult);
                    parsingDailyWeather(apiResult);
            }
        }
    }

    public String downloadWeatherForecast(String urls) {
        HttpURLConnection conn = null;

        try {
            String weatherUrl = setWeatherUrl(urls);
            URL url = new URL(weatherUrl);
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

    public void parsingCurrentWeather(String apiResult) {
        synchronized (this) {

            try {
                JSONObject jsonResult = new JSONObject(apiResult);
                JSONObject currentObject = jsonResult.getJSONObject("current");
                currentWeather = new CurrentWeather();

                currentWeather.setDt(currentObject.optInt("dt"));
                currentWeather.setDate(changeUTCtoDate(currentWeather.getDt()));
                currentWeather.setSunrise(currentObject.optInt("sunrise"));
                currentWeather.setSunset(currentObject.optInt("sunset"));
                currentWeather.setTemp(currentObject.optDouble("temp"));
                currentWeather.setFeels_like(currentObject.optDouble("feels_like"));
                currentWeather.setPressure(currentObject.optInt("pressure"));
                currentWeather.setHumidity(currentObject.optInt("humidity"));
                currentWeather.setDew_point(currentObject.optDouble("dew_point"));
                currentWeather.setUvi(currentObject.optDouble("uvi"));
                currentWeather.setClouds(currentObject.optInt("clouds"));
                currentWeather.setVisibility(currentObject.optInt("visibility"));
                currentWeather.setWind_speed(currentObject.optDouble("wind_speed"));
                currentWeather.setWind_deg(currentObject.optInt("wind_deg"));

                JSONObject weatherObject = currentObject.getJSONArray("weather").getJSONObject(0);
                Weather weather = new Weather();

                weather.setId(weatherObject.optInt("id"));
                weather.setMain(weatherObject.optString("main"));
                weather.setDescription(weatherObject.optString("description"));
                weather.setIcon(weatherObject.optString("icon"));
                currentWeather.setWeather(weather);

                JSONObject rainObject = currentObject.optJSONObject("rain");
                JSONObject snowObject = currentObject.optJSONObject("snow");
                if (rainObject != null) currentWeather.setRain1h(rainObject.optDouble("1h"));
                if (snowObject != null) currentWeather.setRain1h(snowObject.optDouble("1h"));

                notify();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void parsingHourlyWeather(String apiResult) {
        try {
            JSONObject jsonResult = new JSONObject(apiResult);
            JSONArray jsonArray = jsonResult.getJSONArray("hourly");
            hourlyWeathers = new HourlyWeather[48];

            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject hourlyObject = jsonArray.getJSONObject(i);
                HourlyWeather hourlyWeather = new HourlyWeather();

                hourlyWeather.setDt(hourlyObject.optInt("dt"));
                hourlyWeather.setDate(changeUTCtoDate(hourlyWeather.getDt()));
                hourlyWeather.setTemp(hourlyObject.optDouble("temp"));
                hourlyWeather.setFeels_like(hourlyObject.optDouble("feels_like"));
                hourlyWeather.setPressure(hourlyObject.optInt("pressure"));
                hourlyWeather.setHumidity(hourlyObject.optInt("humidity"));
                hourlyWeather.setDew_point(hourlyObject.optDouble("dew_point"));
                hourlyWeather.setUvi(hourlyObject.optDouble("uvi"));
                hourlyWeather.setClouds(hourlyObject.optInt("clouds"));
                hourlyWeather.setVisibility(hourlyObject.optInt("visibility"));
                hourlyWeather.setWind_speed(hourlyObject.optDouble("wind_speed"));
                hourlyWeather.setWind_gust(hourlyObject.optDouble("wind_gust"));
                hourlyWeather.setWind_deg(hourlyObject.optInt("wind_deg"));
                hourlyWeather.setPop(hourlyObject.optDouble("pop"));

                Weather weather = new Weather();
                JSONObject weatherObject = hourlyObject.getJSONArray("weather").getJSONObject(0);
                weather.setId(weatherObject.optInt("id"));
                weather.setMain(weatherObject.optString("main"));
                weather.setDescription(weatherObject.optString("description"));
                weather.setIcon(weatherObject.optString("icon"));
                hourlyWeather.setWeather(weather);

                JSONObject rainObject = hourlyObject.optJSONObject("rain");
                JSONObject snowObject = hourlyObject.optJSONObject("snow");
                if(rainObject != null) hourlyWeather.setRain1h(rainObject.optDouble("1h"));
                if(snowObject != null) hourlyWeather.setRain1h(snowObject.optDouble("1h"));

                hourlyWeathers[i] = hourlyWeather;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parsingDailyWeather(String apiResult) {
        try {
            JSONObject jsonResult = new JSONObject(apiResult);
            JSONArray jsonArray = jsonResult.getJSONArray("daily");
            dailyWeathers = new DailyWeather[7];

            for(int i=0; i<7; i++) {
                JSONObject dailyObject = jsonArray.getJSONObject(i);
                DailyWeather dailyWeather = new DailyWeather();

                dailyWeather.setDt(dailyObject.optInt("dt"));
                dailyWeather.setDate(changeUTCtoDate(dailyWeather.getDt()));
                dailyWeather.setSunrise(dailyObject.optInt("sunrise"));
                dailyWeather.setSunset(dailyObject.optInt("sunset"));
                dailyWeather.setPressure(dailyObject.optInt("pressure"));
                dailyWeather.setHumidity(dailyObject.optInt("humidity"));
                dailyWeather.setDew_point(dailyObject.optDouble("dew_point"));
                dailyWeather.setWind_speed(dailyObject.optDouble("wind_speed"));
                dailyWeather.setClouds(dailyObject.optInt("clouds"));
                dailyWeather.setPop(dailyObject.optDouble("pop"));
                dailyWeather.setUvi(dailyObject.optDouble("uvi"));

                Temperature temp = new Temperature();
                JSONObject tempObject = dailyObject.getJSONObject("temp");
                temp.setMorn(tempObject.optDouble("morn"));
                temp.setDay(tempObject.optDouble("day"));
                temp.setEve(tempObject.optDouble("eve"));
                temp.setNight(tempObject.optDouble("night"));
                temp.setMin(tempObject.optDouble("min"));
                temp.setMax(tempObject.optDouble("max"));
                dailyWeather.setTemp(temp);

                Feels_like feels_like = new Feels_like();
                JSONObject feelsObject = dailyObject.getJSONObject("feels_like");
                feels_like.setMorn(feelsObject.optDouble("morn"));
                feels_like.setDay(feelsObject.optDouble("day"));
                feels_like.setEve(feelsObject.optDouble("eve"));
                feels_like.setNight(feelsObject.optDouble("night"));
                dailyWeather.setFeels_like(feels_like);

                Weather weather = new Weather();
                JSONObject weatherObject = dailyObject.getJSONArray("weather").getJSONObject(0);
                weather.setId(weatherObject.optInt("id"));
                weather.setMain(weatherObject.optString("main"));
                weather.setDescription(weatherObject.optString("description"));
                weather.setIcon(weatherObject.optString("icon"));
                dailyWeather.setWeather(weather);

                Double rain = dailyObject.optDouble("rain");
                Double snow = dailyObject.optDouble("snow");
                if(rain != null) dailyWeather.setRain(rain);
                if(snow != null) dailyWeather.setSnow(snow);

                dailyWeathers[i] = dailyWeather;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // UTC를 date로 변환
    // 2021-01-04 05:00:00
    public String changeUTCtoDate(int dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String koreaDate = sdf.format(date);

        return koreaDate;
    }
}