package com.heon9u.alarm_weather_app.alarm;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.dto.Ringtone;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class RingtoneMediaAdapter extends RecyclerView.Adapter<RingtoneMediaAdapter.RingtoneViewHolder> {

    public int selectedItem = -1;
    private Context context;
    private ArrayList<Ringtone> ringtoneList;

    public RingtoneMediaAdapter() { }

    public RingtoneMediaAdapter(Context context, ArrayList<Ringtone> ringtoneList) {
        this.context = context;
        this.ringtoneList = ringtoneList;
    }

    @NonNull
    @Override
    public RingtoneMediaAdapter.RingtoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ringtone_item, parent, false);

        return new RingtoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RingtoneMediaAdapter.RingtoneViewHolder holder, int position) {
        String title = ringtoneList.get(position).getTitle();
        holder.title.setText(title);
        holder.radioButton.setChecked(position == selectedItem);
    }

    @Override
    public int getItemCount() {
        if(ringtoneList == null) return 0;
        return ringtoneList.size();
    }

    public class RingtoneViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        RadioButton radioButton;
        TextView title;

        public RingtoneViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            radioButton = itemView.findViewById(R.id.radioButton);
            title = itemView.findViewById(R.id.title);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItem = getBindingAdapterPosition();
                    Uri uri = Uri.parse(ringtoneList.get(selectedItem).getUri());
                    if(RingtoneListActivity.mediaPlayer != null)
                        RingtoneListActivity.stopMediaPlayer();
                    RingtoneListActivity.startMediaPlayer(context, uri);
                    notifyDataSetChanged();
                }
            };
            radioButton.setOnClickListener(clickListener);
            title.setOnClickListener(clickListener);
        }
    }
}
