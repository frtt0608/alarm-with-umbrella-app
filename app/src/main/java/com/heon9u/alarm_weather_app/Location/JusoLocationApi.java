package com.heon9u.alarm_weather_app.Location;

import android.os.AsyncTask;

import com.heon9u.alarm_weather_app.Dto.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JusoLocationApi extends AsyncTask<String, Void, String> {

    List<Location> locations;
    boolean isFinish = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadJusoLocation(urls[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "juso location URL 접근 실패";
        }
    }

    public String downloadJusoLocation(String urls) {
        HttpURLConnection conn = null;

        try {
            String jusoUrl = urls + "&currentPage=1" + "&countPerPage=20" + "&resultType=json";
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

                return sb.toString();
            } else {
                System.out.println("juso location HTTP failed");
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
                JSONArray jsonArray = jsonResult.getJSONArray("juso");
                locations = new ArrayList<>();

                for(int i=0; i<jsonArray.length(); i++) {
                    if(i == 20) break;

                    JSONObject jusoObject = jsonArray.getJSONObject(i);
                    Location location = new Location();

                    location.setStreetAddress(jusoObject.optString("roadAddrPart1"));
                    location.setLotAddress(jusoObject.optString("jibunAddr"));
                    location.setCommunityCenter(jusoObject.optString("hemdNm"));

                    locations.add(location);
                }

                isFinish = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
