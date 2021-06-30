package com.heon9u.alarm_weather_app.alarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.dto.Ringtone;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class RingtoneMediaFragment extends Fragment {

    Button save;
    RecyclerView recyclerView;
    ArrayList<Ringtone> ringtoneList;
    RingtoneMediaAdapter ringtoneMediaAdapter;
    TextView noMedia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ringtone_media, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ringtoneMediaAdapter = new RingtoneMediaAdapter(getContext(), ringtoneList);
        recyclerView.setAdapter(ringtoneMediaAdapter);

        save = view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ringtoneMediaAdapter.selectedItem;
                if(index == -1) {
                    Toast.makeText(getContext(),
                            "선택하신 알람음이 없습니다. 종료하시려면 뒤로가기를 눌러주세요",
                            Toast.LENGTH_SHORT).show();
                } else {
                    transmitRingtone(index);
                }
            }
        });

        noMedia = view.findViewById(R.id.noMedia);
        if(ringtoneList.size() == 0) {
            noMedia.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noMedia.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ringtoneList = new ArrayList<>();
        getMediaStore();
    }

    public void getMediaStore() {
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE
        };

        Cursor cursor = getContext().getContentResolver().query(externalUri, projection,
                null, null,
                MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor.getCount() == 0) {
            Log.e("audio", "cursor null or cursor is empty");
        } else {
            while(cursor.moveToNext()) {
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                String contentUri = externalUri.toString() + "/" + id;

                ringtoneList.add(new Ringtone(title, contentUri));
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
