package com.heon9u.alarm_weather_app.Location;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class JusoCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private final String jusoUrl = "http://juso.go.kr/addrlink/addrLinkApi.do";
    private final String confmKey = "devU01TX0FVVEgyMDIxMDIwODAxMjIzMTExMDc4OTc=";

    TextView errorMessage;
    EditText searchAddress;
    AppCompatButton searchAddressButton;
    RecyclerView recyclerView;
    ArrayList<Location> searchLocationResultList;
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_create_juso);

        errorMessage = findViewById(R.id.errorMessage);
        searchAddress = findViewById(R.id.searchAddress);
        searchAddressButton = findViewById(R.id.searchAddressButton);
        searchAddressButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initAdMob();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchAddressButton:
                searchJusoLocationApi();
                setRecyclerView();
                break;
        }
    }

    public void searchJusoLocationApi() {
        String keyword = searchAddress.getText().toString();
        String locationUrl = jusoUrl + "?confmKey=" + confmKey + "&keyword=" + keyword;

        searchLocationResultList = new ArrayList<>();
        JusoLocationApi jusoLocationApi = new JusoLocationApi(locationUrl);
        jusoLocationApi.executeURL();

        while(!jusoLocationApi.isFinish) {}
        
        if(jusoLocationApi.isError) {
            searchLocationResultList.clear();
            errorMessage.setText("주소를 좀 더 상세히 입력해주세요.");
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            searchLocationResultList = new ArrayList<>(jusoLocationApi.locations);
            errorMessage.setVisibility(View.GONE);
        }
    }

    public void setRecyclerView() {
        JusoAdapter jusoAdapter = new JusoAdapter(getApplicationContext(),
                this,
                searchLocationResultList);
        recyclerView.setAdapter(jusoAdapter);
    }

    public void initAdMob() {
        MobileAds.initialize(this, initializationStatus -> { });

        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.sample_banner));
        frameLayout.addView(adView);
        loadBanner();
    }

    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
