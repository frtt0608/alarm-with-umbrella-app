package com.heon9u.alarm_weather_app.Alarm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.Location.LocationListView;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class AlarmListView extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    AppCompatImageButton createAlarm, resetLocation;
    AlarmAdapter alarmAdapter;
    AlarmDatabase alarmDB;

    ArrayList<Alarm> alarmList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // RecyclerView로 alarm 리스트 페이지 호출
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // alarm 생성 버튼
        createAlarm = view.findViewById(R.id.createAlarm);
        createAlarm.setOnClickListener(this);
        resetLocation = view.findViewById(R.id.manageLocation);
        resetLocation.setOnClickListener(this);

        alarmDB = new AlarmDatabase(getContext());
        takeAdapter();

        return view;
    }

    public void takeAdapter() {
        displayAlarm();
        alarmAdapter = new AlarmAdapter(getActivity(), alarmList);
        recyclerView.setAdapter(alarmAdapter);
    }

    void displayAlarm() {
        alarmList = new ArrayList<>();
        Cursor cursor = alarmDB.readAllAlarm();

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
        takeAdapter();
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
                createAlarmIntent.putExtra("REQUEST_STATE", "create");
                startActivity(createAlarmIntent);
                break;
            case R.id.manageLocation:
                Intent menuLocationIntent = new Intent(getActivity(), LocationListView.class);
                startActivity(menuLocationIntent);
                break;
        }
    }
}
