package com.heon9u.alarm_weather_app.Alarm;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.heon9u.alarm_weather_app.R;

public class RingtoneListActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    public static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringtone_list);

        mediaPlayer = null;
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        prepareViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mediaPlayer != null)
                    stopMediaPlayer();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void prepareViewPager(ViewPager viewPager) {
        RingtoneListAdapter ringtoneListAdapter = new RingtoneListAdapter(getSupportFragmentManager());
        ringtoneListAdapter.addFragment(new RingtoneManagerFragment(), "기본 벨소리");
        ringtoneListAdapter.addFragment(new RingtoneMediaFragment(), "저장된 음악");

        viewPager.setAdapter(ringtoneListAdapter);
    }

    public static void startMediaPlayer(Context context, Uri uri) {
        try {
            Log.d("MediaPlayer", uri.toString());
            if(mediaPlayer == null)
                mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.setOnPreparedListener(mp -> mp.start());
            mediaPlayer.setOnCompletionListener(mp -> mp.release());
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMediaPlayer() {
        if(mediaPlayer != null) {
            Log.d("MediaPlayer", "stopMedia");
//            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
