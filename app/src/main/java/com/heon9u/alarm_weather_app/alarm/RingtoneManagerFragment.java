package com.heon9u.alarm_weather_app.alarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.dto.Ringtone;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class RingtoneManagerFragment extends Fragment {

    Button save;
    RecyclerView recyclerView;
    ArrayList<Ringtone> ringtoneList;
    RingtoneManagerAdapter ringtoneAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ringtone_manager, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ringtoneAdapter = new RingtoneManagerAdapter(getContext(), ringtoneList);
        recyclerView.setAdapter(ringtoneAdapter);

        save = view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ringtoneAdapter.selectedItem;
                if(index == -1) {
                    Toast.makeText(getContext(),
                            "선택하신 알람음이 없습니다. 종료하시려면 뒤로가기를 눌러주세요",
                            Toast.LENGTH_SHORT).show();
                } else {
                    transmitRingtone(index);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ringtoneList = new ArrayList<>();
        getBasicAlarm();
    }

    private void getBasicAlarm() {
        // content://media/external_primary/audio/media/21?title=Castle&canonical=1

        RingtoneManager ringtoneManager = new RingtoneManager(getActivity());
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();

        if(cursor.getCount() == 0) {
            Log.e("TAG", "cursor null or cursor is empty");
        } else {
            while(cursor.moveToNext()) {
                int pos = cursor.getPosition();
                String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                Uri uri = ringtoneManager.getRingtoneUri(pos);

                ringtoneList.add(new Ringtone(title, uri.toString()));
            }
        }
        cursor.close();
    }

    public void transmitRingtone(int index) {
        Ringtone ringtone = ringtoneList.get(index);
        Intent intent = new Intent();
        intent.putExtra("Ringtone", ringtone);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RingtoneListActivity.stopMediaPlayer();
    }
}
