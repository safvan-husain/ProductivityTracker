package com.example.productivitytracker;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<DataModel> itemList;

    public CustomAdapter(Context context, List<DataModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_tile, null);
        }

        DataModel item = itemList.get(position);

        TextView descriptionTextView = view.findViewById(R.id.description);
        TextView durationTextView = view.findViewById(R.id.duration);

        descriptionTextView.setText(item.getDescription());
        durationTextView.setText(formatDuration(item.getDuration()));

        return view;
    }

    private String formatDuration(int durationInMillis) {
        // Format the duration as you prefer, e.g.,
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(durationInMillis);

        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }
}
