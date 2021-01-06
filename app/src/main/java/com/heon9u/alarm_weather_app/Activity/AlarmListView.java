package com.heon9u.alarm_weather_app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class AlarmListView extends Fragment {

    private RecyclerView recyclerView;
    AppAdapter appAdapter;
    Button createAlarm;

    AppDatabaseHelper appDB;
    ArrayList<String> alarm_id, alarm_time;

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
        createAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(getActivity(), CreateAlarmActivity.class);
                startActivity(createIntent);
            }
        });

        appDB = new AppDatabaseHelper(getActivity());
        alarm_id = new ArrayList<>();
        alarm_time = new ArrayList<>();
        displayData();

        appAdapter = new AppAdapter(getActivity(), alarm_id, alarm_time);
        recyclerView.setAdapter(appAdapter);

        return view;
    }

    void displayData() {
        Cursor cursor = appDB.readAllData();

        if(cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No alarm", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                alarm_id.add(cursor.getString(0));
                alarm_time.add(cursor.getString(1));
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
