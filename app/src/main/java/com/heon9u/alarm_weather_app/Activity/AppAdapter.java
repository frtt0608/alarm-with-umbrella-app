package com.heon9u.alarm_weather_app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder>{

    Context context;
    ArrayList alarm_id, alarm_time;

    private LayoutInflater layoutInflater;

    AppAdapter(Context context, ArrayList alarm_id, ArrayList alarm_time) {
        this.context = context;
        this.alarm_id = alarm_id;
        this.alarm_time = alarm_time;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alarm_item, parent, false);

        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        String id = String.valueOf(alarm_id.get(position));
        holder.alarm_id.setText(String.valueOf(alarm_id.get(position)));
        holder.alarm_time.setText(String.valueOf(alarm_time.get(position)));

        DeleteListener deleteListener = new DeleteListener();
        deleteListener.applyData(position, id);
        holder.delete_button.setOnClickListener(deleteListener);
    }

    public class DeleteListener implements View.OnClickListener {

        int position;
        String id;

        void applyData(int position, String id) {
            this.position = position;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete");
            builder.setMessage("Are you sure to delete " + id + " ??");
            builder.setIcon(android.R.drawable.ic_menu_delete);

            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppDatabaseHelper appDB = new AppDatabaseHelper(context);
                    int result = appDB.deleteAlarm(id);

                    if(result > 0) {
                        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                        alarm_id.remove(position);
                        alarm_time.remove(position);
                        notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("No", null);
            builder.show();
        }
    }

    @Override
    public int getItemCount() {
        return alarm_id.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder {

        TextView alarm_id, alarm_time;
        ImageButton delete_button;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            alarm_id = itemView.findViewById(R.id.alarm_id);
            alarm_time = itemView.findViewById(R.id.alarm_time);
            delete_button = itemView.findViewById(R.id.delete_button);
        }
    }
}
