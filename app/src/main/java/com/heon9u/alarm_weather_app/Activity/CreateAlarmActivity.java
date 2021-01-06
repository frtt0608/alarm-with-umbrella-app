package com.heon9u.alarm_weather_app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.R;

public class CreateAlarmActivity extends AppCompatActivity {

    EditText time_input;
    Button create_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_create);

        time_input = findViewById(R.id.time_input);
        create_button = findViewById(R.id.create_button);
        cancel_button = findViewById(R.id.cancel_button);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabaseHelper appDB = new AppDatabaseHelper(CreateAlarmActivity.this);
                appDB.createAlarm(time_input.getText().toString());
                finish();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
