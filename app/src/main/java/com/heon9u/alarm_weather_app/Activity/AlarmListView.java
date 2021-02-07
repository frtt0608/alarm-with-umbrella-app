package com.heon9u.alarm_weather_app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlarmListView extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private GpsTracker gpsTracker;
    TextView location;
    AppCompatImageButton createAlarm, resetLocation;
    AppAdapter appAdapter;
    AppDatabaseHelper appDB;

    ArrayList<Alarm> alarmList;
    double latitude, longitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // RecyclerView로 alarm 리스트 페이지 호출
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // 현재 위치
        location = view.findViewById(R.id.location);
        searchCurrentLocation();

        // alarm 생성 버튼
        createAlarm = view.findViewById(R.id.createAlarm);
        createAlarm.setOnClickListener(this);
        resetLocation = view.findViewById(R.id.resetLocation);
        resetLocation.setOnClickListener(this);

        appDB = new AppDatabaseHelper(getActivity());
        displayAlarm();
        appAdapter = new AppAdapter(getActivity(), alarmList);
        recyclerView.setAdapter(appAdapter);

        return view;
    }

    void searchCurrentLocation() {
        gpsTracker = new GpsTracker(getContext());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        Log.d("AlarmListView", latitude + ", " + longitude);

        String currentAddress = getCurrentAddress(latitude, longitude);
        String[] address = getCurrentAddress(latitude, longitude).split(" ");

        String addr = "";

        for(int i=1; i<=3; i++) {
            if(i < address.length) {
                addr += address[i] + " ";
            }
        }
        location.setText(addr);
        gpsTracker.stopGpsTracker();
        Toast.makeText(getContext(), addr, Toast.LENGTH_SHORT).show();

        Log.d("AlarmListView", currentAddress);
    }

    public String getCurrentAddress(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(getContext(), Locale.KOREAN);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);

        } catch (IOException ioException) {
            return "x 네트워크 에러";
        } catch (IllegalArgumentException illegalArgumentException) {
            return "x GPS 좌표 에러";
        }

        if (addresses == null || addresses.size() == 0) {
            return "x 주소를 찾지 못했습니다.";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0);
    }

    void displayAlarm() {
        alarmList = new ArrayList<>();
        Cursor cursor = appDB.readAllAlarm();

        if(cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No alarm", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                Alarm alarm = setAlarm(cursor);
                alarmList.add(alarm);
            }
        }
    }

    public Alarm setAlarm(Cursor cursor) {
        Alarm alarm = new Alarm();

        alarm.setId(cursor.getInt(0));
        alarm.setHour(cursor.getInt(1));
        alarm.setMinute(cursor.getInt(2));
        alarm.setTitle(cursor.getString(3));
        alarm.setTotalFlag(cursor.getInt(4) > 0);
        alarm.setAllDayFlag(cursor.getInt(5) > 0);
        alarm.setDay(cursor.getString(6));
        alarm.setVolume(cursor.getInt(7));
        alarm.setBasicSoundFlag(cursor.getInt(8) > 0);
        alarm.setBasicSound(cursor.getString(9));
        alarm.setUmbSoundFlag(cursor.getInt(10) > 0);
        alarm.setUmbSound(cursor.getString(11));
        alarm.setVibFlag(cursor.getInt(12) > 0);
        alarm.setLocation_id(cursor.getInt(13));

        return alarm;
    }


    @Override
    public void onStart() {
        super.onStart();
        displayAlarm();
        appAdapter = new AppAdapter(getActivity(), alarmList);
        recyclerView.setAdapter(appAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.createAlarm:
                Intent createAlarmIntent = new Intent(getActivity(), AlarmSetActivity.class);
                startActivity(createAlarmIntent);
                break;
            case R.id.resetLocation:
                searchCurrentLocation();
                break;
        }
    }
}
