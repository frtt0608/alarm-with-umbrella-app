package com.heon9u.alarm_weather_app.location;

import android.util.Log;

import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.dto.LocationBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JusoLocationApi {

    String locationUrl;
    String apiResult;
    volatile boolean isFinish;
    boolean isError;
    List<Location> locationList;

    public JusoLocationApi(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public void executeURL() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadJuso();
                parsingJSON();
            }
        }).start();
    }

    public void downloadJuso() {
        HttpURLConnection conn = null;

        try {
            String jusoUrl = locationUrl + "&currentPage=1" + "&countPerPage=20" + "&resultType=json";
            URL url = new URL(jusoUrl);
            conn = (HttpURLConnection) url.openConnection();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();

                String line;

                while((line = br.readLine()) != null) {
                    sb.append(line);
                }

                apiResult = sb.toString();
            } else {
                Log.e("jusoLocationApi", "juso location HTTP failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public void parsingJSON() {
        if(apiResult != null) {
            try {
                JSONObject jsonResult = new JSONObject(apiResult);
                jsonResult = jsonResult.getJSONObject("results");
                String errorMessage = jsonResult.getJSONObject("common").optString("errorMessage");

                if(errorMessage.equals("정상")) {
                    isError = false;
                    JSONArray jsonArray = jsonResult.getJSONArray("juso");
                    locationList = new ArrayList<>();
                    Location location;

                    for(int i=0; i<jsonArray.length(); i++) {
                        if (i == 20) break;

                        JSONObject jusoObject = jsonArray.getJSONObject(i);
                        location = new LocationBuilder()
                                .setStreetAddress(jusoObject.optString("roadAddrPart1"))
                                .setLotAddress(jusoObject.optString("jibunAddr"))
                                .setCommunityCenter(jusoObject.optString("hemdNm"))
                                .build();

                        locationList.add(location);
                    }
                } else {
                    isError = true;
                }

                isFinish = true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
