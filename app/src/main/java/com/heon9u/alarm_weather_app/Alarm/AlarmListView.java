package com.heon9u.alarm_weather_app.Alarm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    AppCompatImageButton createAlarm, manageLocation;
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
        manageLocation = view.findViewById(R.id.manageLocation);
        manageLocation.setOnClickListener(this);

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
            Toast.makeText(getActivity(),
                    "설정된 알람이 없습니다.", Toast.LENGTH_SHORT).show();
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
        alarm.setBasicSoundTitle(cursor.getString(9));
        alarm.setBasicSoundUri(cursor.getString(10));

        alarm.setUmbSoundFlag(cursor.getInt(11) > 0);
        alarm.setUmbSoundTitle(cursor.getString(12));
        alarm.setUmbSoundUri(cursor.getString(13));

        alarm.setVibFlag(cursor.getInt(14) > 0);
        alarm.setLocation_id(cursor.getInt(15));

        return alarm;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkNewAlarm();
    }

    public void checkNewAlarm() {
        Cursor cursor = alarmDB.getItemCount();
        cursor.moveToLast();
        int savedAlarmCount = cursor.getInt(0);

        // alarm 추가한 경우
        if(alarmList.size() < savedAlarmCount) {
            cursor = alarmDB.readLastAlarm();
            cursor.moveToLast();
            Alarm alarm = setAlarm(cursor);
            alarmAdapter.addItem(alarm);
        }
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
