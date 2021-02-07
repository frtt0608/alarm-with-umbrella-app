package com.heon9u.alarm_weather_app.Location;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.R;

public class LocationCreateActivity extends AppCompatActivity {

//    devU01TX0FVVEgyMDIxMDIwODAxMjIzMTExMDc4OTc=

    WebView searchAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_create);


    }

    public void init_WebView() {
        searchAddress = findViewById(R.id.searchAddress);
        searchAddress.getSettings().setJavaScriptEnabled(true); // js허용
        searchAddress.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // window.open 허용
        searchAddress.setWebChromeClient(new WebChromeClient()); // web client는 chrome으로 설정
        searchAddress.addJavascriptInterface(new WebViewInterface(), "SearchAddress");
        searchAddress.loadUrl("https://www.juso.go.kr/addrlink/addrMobileLinkUrl.do");

    }

    public class WebViewInterface {
        @JavascriptInterface
        public void applyAddress(String zoneCode, String roadAddress, String buildingName) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String address = String.format("%s %s %s", zoneCode, roadAddress, buildingName);
                    Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
                    init_WebView();
                }
            });
        }
    }
}
