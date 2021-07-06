package com.heon9u.alarm_weather_app.alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.alarm.database.AlarmViewModel;
import com.heon9u.alarm_weather_app.anotherTools.AdNativeClass;
import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.location.LocationListView;
import com.heon9u.alarm_weather_app.R;
import com.heon9u.alarm_weather_app.databinding.AlarmFragmentBinding;

import java.util.List;

public class AlarmListView extends Fragment {
    public static final int CREATE_ALARM_REQUEST = 1;
    public static final int UPDATE_ALARM_REQUEST = 2;

    public AlarmViewModel alarmViewModel;
    public AlarmFragmentBinding alarmFragmentBinding;
    Context context;
    RecyclerView recyclerView;
    TextView noAlarmText;
    AlarmAdapter alarmAdapter;
    CardView adContainer;
    AdNativeClass adNativeClass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        alarmFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.alarm_fragment, container, false);
        alarmFragmentBinding.setAlarmClick(this);
        View view = alarmFragmentBinding.getRoot();

        adContainer = view.findViewById(R.id.adContainer);
        noAlarmText = view.findViewById(R.id.noAlarmText);

        alarmAdapter = new AlarmAdapter();

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(alarmAdapter);

        attachItemTouchHelperToAdapter();

        alarmAdapter.setOnItemClickListener(alarm -> {
            Intent updateIntent = new Intent(getActivity(), AlarmSetActivity.class);
            updateIntent.putExtra("alarm", alarm);
            updateIntent.putExtra("REQUEST_STATE", "update");
            startActivityForResult(updateIntent, UPDATE_ALARM_REQUEST);
        });

        alarmAdapter.setOnCheckedChangeListener((alarm) -> {
            alarmViewModel.update(alarm);
            String requestMsg = alarm.isTotalFlag() ? "reboot":"cancel";
            updateAlarmManager(alarm, requestMsg);
        });

        initAdMob();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        alarmViewModel.getAllAlarms().observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                alarmAdapter.submitList(alarms);

                if(alarms.size() > 0) showAlarmList();
                else hideAlarmList();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void createAlarmClick(View view) {
        Intent createAlarmIntent = new Intent(getActivity(), AlarmSetActivity.class);
        createAlarmIntent.putExtra("REQUEST_STATE", "create");
        startActivityForResult(createAlarmIntent, CREATE_ALARM_REQUEST);
    }

    public void manageLocationClick(View view) {
        Intent menuLocationIntent = new Intent(getActivity(), LocationListView.class);
        startActivity(menuLocationIntent);
    }

    public void updateAlarmManager(Alarm alarm, String request) {
        Intent alarmIntent = new Intent(getActivity(), AlarmManagerActivity.class);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("request", request);
        startActivity(alarmIntent);
    }

    public void attachItemTouchHelperToAdapter() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                showDialogDeleteAlarm(viewHolder);
            }

        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != -1) return;

        Alarm alarm = (Alarm) data.getSerializableExtra("alarm");

        switch(requestCode) {
            case CREATE_ALARM_REQUEST:
                alarmViewModel.insert(alarm);
                Toast.makeText(context,
                        alarm.getHour() + "시 " + alarm.getMinute() + "분에 알람을 설정하였습니다.",
                        Toast.LENGTH_SHORT).show();
                break;
            case UPDATE_ALARM_REQUEST:
                alarmViewModel.update(alarm);
                Toast.makeText(context,
                        "알람을 수정했습니다.",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showDialogDeleteAlarm(@NonNull RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("알람 삭제")
                .setMessage("해당 알람을 삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setCancelable(false)
                .setPositiveButton("삭제", (dialog, which) -> {
                    Alarm alarm = alarmAdapter.getAlarmAt(viewHolder.getBindingAdapterPosition());
                    updateAlarmManager(alarm, "cancel");
                    alarmViewModel.delete(alarm);
                    Toast.makeText(getContext(), "알람을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("취소", (dialog, which) -> {
                    alarmAdapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
                    Toast.makeText(getContext(), "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                }).show();
    }

    @Override
    public void onDestroy() {
        if(adNativeClass.nativeAd != null) {
            adNativeClass.nativeAd.destroy();
        }

        super.onDestroy();
    }

    public void showAlarmList() {
        noAlarmText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void hideAlarmList() {
        noAlarmText.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void initAdMob() {
        adNativeClass = new AdNativeClass(getContext());
        adNativeClass.initAdMob();
        adNativeClass.setNativeAdMob(adContainer);
    }
}
