package com.heon9u.alarm_weather_app.location;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JusoCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private final String jusoUrl = "http://juso.go.kr/addrlink/addrLinkApi.do";
    private final String confmKey = "U01TX0FVVEgyMDIxMDUxMjAwMjE1NDExMTE0ODg=";

    TextView errorMessage;
    EditText searchAddress;
    AppCompatImageButton searchAddressButton;
    JusoAdapter jusoAdapter;
    RecyclerView recyclerView;
    List<Location> resultLocationList;

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

        resultLocationList = new ArrayList<>();
        JusoLocationApi jusoLocationApi = new JusoLocationApi(locationUrl);
        jusoLocationApi.executeURL();

        while(!jusoLocationApi.isFinish) {}

        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        if(jusoLocationApi.isError) {
            resultLocationList.clear();
            errorMessage.setText("주소를 좀 더 상세히 입력해주세요.");
        } else {
            resultLocationList = new ArrayList<>(jusoLocationApi.locationList);
            if(resultLocationList.size() == 0) {
                errorMessage.setText("검색 결과가 없습니다. \n" +
                                        "다시 한번 주소를 확인해주세요.");
            } else {
                errorMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRecyclerView() {
        jusoAdapter = new JusoAdapter(resultLocationList);
        recyclerView.setAdapter(jusoAdapter);

        jusoAdapter.setOnItemClickListener(location -> {
            setGeocoding(location);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Location", location);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    public void setGeocoding(Location juso) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> address;

        try {
            // 도로명주소가 없을 때는 지번주소 이용하기.
            if (juso.getStreetAddress() == null) {
                address = geocoder.getFromLocationName(juso.getLotAddress(), 1);
            } else {
                address = geocoder.getFromLocationName(juso.getStreetAddress(), 1);
            }

            juso.setLatitude(address.get(0).getLatitude());
            juso.setLongitude(address.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initAdMob() {
        MobileAds.initialize(this, initializationStatus -> { });

        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        Display display = getWindowManager().getDefaultDisplay();
        AdBannerClass adBannerClass = new AdBannerClass(getApplicationContext(), display);
        frameLayout.addView(adBannerClass.adView);
    }
}
