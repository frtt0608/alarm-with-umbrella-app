package com.heon9u.alarm_weather_app.Location;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class JusoCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private final String jusoUrl = "http://juso.go.kr/addrlink/addrLinkApi.do";
    private final String confmKey = "devU01TX0FVVEgyMDIxMDIwODAxMjIzMTExMDc4OTc=";

    EditText searchAddress;
    AppCompatButton searchAddressButton;
    RecyclerView recyclerView;
    ArrayList<Location> searchLocationResultList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_create_juso);

        searchAddress = findViewById(R.id.searchAddress);
        searchAddressButton = findViewById(R.id.searchAddressButton);
        searchAddressButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchAddressButton:
                Log.d("jusoCreate", "시작");
                searchJusoLocationApi();
                Log.d("jusoCreate", "recyclerView");
                setRecyclerView();
                Log.d("jusoCreate", "recyclerView adapter 완료");
                break;
        }
    }

    public void searchJusoLocationApi() {
        String keyword = searchAddress.getText().toString();
        String locationUrl = jusoUrl + "?confmKey=" + confmKey + "&keyword=" + keyword;

        JusoLocationApi jusoLocationApi = new JusoLocationApi(locationUrl);
        jusoLocationApi.executeURL();
        
        while(!jusoLocationApi.isFinish) {}
        Log.d("jusoCreate", "api 저장 완료");

        searchLocationResultList = new ArrayList<>(jusoLocationApi.locations);
        Log.d("jusoCreate", searchLocationResultList.get(0).toString());
    }

    public void setRecyclerView() {
        JusoAdapter jusoAdapter = new JusoAdapter(getApplicationContext(),
                this,
                searchLocationResultList);
        recyclerView.setAdapter(jusoAdapter);
    }
}
