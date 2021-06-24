package com.heon9u.alarm_weather_app.Openweather;

import android.os.AsyncTask;
import android.util.Log;

import com.heon9u.alarm_weather_app.Dto.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


// TM좌표 조회: getTMStdrCrdnt
// 근접 측정소 목록 조회: getNearbyMsrstnList


public class TmPosAndStationNameApi extends AsyncTask<String, Void, String> {
    private final String serviceKey = "iNSyWM3QGRc1%2B1ePlvp%2BAY0mZWRBAKGono6w8xfXe%2F%2Fs4pjupyF0wGgKWY6soqbZgerMYzi8l4r74LlsMU2i4A%3D%3D";
    private final String msrstnUrl = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc";
    private final String arpltnUrl = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc";
    private final int TM = 2;
    private final int NEARBY = 1;

    public Location juso;

    int apiType;
    String umdName, sidoName;

    public TmPosAndStationNameApi(int apiType) {
        this.apiType = apiType;
    }

    public TmPosAndStationNameApi(int apiType, Location juso) {
        this.apiType = apiType;
        this.juso = juso;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadTMPosition(urls[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "Location TM Position URL 접근 실패";
        }
    }

    @Override
    protected void onPostExecute(String apiResult) {
        super.onPostExecute(apiResult);

        if (apiResult != null) {
            switch (apiType) {
                case TM:
                    setLocationTMPosition(apiResult);
                    break;
            }
        }
    }

    public void getSidoNameAndUmdName(String jibunAddr) {
        String[] addrs = jibunAddr.split(" ");
        sidoName = addrs[0];

        for (int i = 1; i < addrs.length; i++) {
            String finalStr = addrs[i].substring(addrs[i].length() - 1);
            if (finalStr.equals("읍") || finalStr.equals("면") || finalStr.equals("동")) {
                umdName = addrs[i];
                break;
            }
        }
    }

    public String downloadTMPosition(String urls) {
        HttpURLConnection conn = null;

        try {
            getSidoNameAndUmdName(juso.getLotAddress());
            StringBuilder urlBuilder = new StringBuilder(msrstnUrl + urls);
            urlBuilder.append("?serviceKey=").append(serviceKey).append("&numOfRows=10").append("&pageNo=1");
            urlBuilder.append("&returnType=json").append("&umdName=").append(URLEncoder.encode(umdName, "UTF-8"));
            URL url = new URL(urlBuilder.toString());
            conn = (HttpURLConnection) url.openConnection();

            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                Log.e("FineDustApi", "msrstnUrl HTTP failed");
            }

            StringBuffer sb = new StringBuffer();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return null;
    }

    public void setLocationTMPosition(String apiResult) {

        synchronized (this) {
            try {
                JSONObject jsonResult = new JSONObject(apiResult);
                jsonResult = jsonResult.getJSONObject("response");
                JSONObject jsonHeader = jsonResult.getJSONObject("header");

                if (jsonHeader.optString("resultCode").equals("00")) {
                    JSONArray jsonArray = jsonResult.getJSONObject("body").getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jusoObject = jsonArray.getJSONObject(i);
                        String sido = jusoObject.optString("sidoName");
                        String umd = jusoObject.optString("umdName");

                        if (sidoName.equals(sido) && umdName.equals(umd)) {
                            juso.setTmX(jusoObject.optDouble("tmX"));
                            juso.setTmY(jusoObject.optDouble("tmY"));
                            break;
                        }
                    }
                }

                notify();
            } catch (Exception e) {
                Log.e("FineDustApi", "정확한 TM 좌표를 찾을 수 잆습니다.");
            }
        }
    }
}
