package com.heon9u.alarm_weather_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.R;

public class AlarmListView extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] langs = {"Java", "C", "C++", "Python",
                                "Kotlin", "React", "Flutter",
                                "Javascript", "Vue", "R"};
    Button createAlarm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // RecyclerView로 alarm 리스트 페이지 호출
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new AppAdapter(langs);
        recyclerView.setAdapter(adapter);

        // alarm 생성 버튼
        createAlarm = (Button) view.findViewById(R.id.createAlarm);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void createAlarmListener() {
        Intent createIntent = new Intent(getActivity(), CreateAlarmActivity.class);
        startActivity(createIntent);
    }
}
