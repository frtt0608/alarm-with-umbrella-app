package com.heon9u.alarm_weather_app.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Ringtone;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class RingtoneMediaFragment extends Fragment {

    AppCompatButton save;
    RecyclerView recyclerView;
    ArrayList<Ringtone> ringtoneList;
    RingtoneMediaAdapter ringtoneMediaAdapter;

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
                    Log.e("MyMediaFragment", "index is -1, you don't choice anything");
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//          getDownloads();
        } else {
            getMediaStore();
        }
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
