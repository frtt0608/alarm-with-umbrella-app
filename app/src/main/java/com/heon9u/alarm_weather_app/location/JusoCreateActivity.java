package com.heon9u.alarm_weather_app.location;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.MobileAds;
import com.heon9u.alarm_weather_app.anotherTools.AdBannerClass;
import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class JusoCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private final String jusoUrl = "http://juso.go.kr/addrlink/addrLinkApi.do";
    private final String confmKey = "U01TX0FVVEgyMDIxMDUxMjAwMjE1NDExMTE0ODg=";

    TextView errorMessage;
    EditText searchAddress;
    AppCompatImageButton searchAddressButton;
    RecyclerView recyclerView;
    ArrayList<Location> searchLocationResultList;

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
        keyword = keyword.replaceAll(" ", "%20");
        String locationUrl = jusoUrl + "?confmKey=" + confmKey + "&keyword=" + keyword;

        searchLocationResultList = new ArrayList<>();
        JusoLocationApi jusoLocationApi = new JusoLocationApi(locationUrl);
        jusoLocationApi.executeURL();

        while(!jusoLocationApi.isFinish) {}

        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        if(jusoLocationApi.isError) {
            searchLocationResultList.clear();
            errorMessage.setText("주소를 좀 더 상세히 입력해주세요.");
        } else {
            searchLocationResultList = new ArrayList<>(jusoLocationApi.locationList);
            if(searchLocationResultList.size() == 0) {
                errorMessage.setText("검색 결과가 없습니다. \n" +
                                        "다시 한번 주소를 확인해주세요.");
            } else {
                errorMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
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
        Display display = getWindowManager().getDefaultDisplay();
        AdBannerClass adBannerClass = new AdBannerClass(getApplicationContext(), display);
        frameLayout.addView(adBannerClass.adView);
    }
}